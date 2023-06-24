package com.plato.recoserver.recoserver.datamanager.dao.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.exceptions.JedisExhaustedPoolException;
import redis.clients.jedis.util.Pool;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kevin
 * @date 2022-03-04
 */
@Slf4j
public class JedisMasterSlavePool extends JedisPoolAbstract {

    private volatile List<HostAndPort> slaveAddrs = new ArrayList<>();
    private volatile Map<HostAndPort, JedisPool> slavePools = new HashMap<>();
    private static Map<Jedis, Pool<Jedis>> allocatedMap = new ConcurrentHashMap<>();

    public JedisMasterSlavePool(GenericObjectPoolConfig<Jedis> poolConfig, Set<URI> uris) {
        boolean hasMaster = false;
        for (URI uri : uris) {
            if (isMaster(uri)) {
                hasMaster = true;
                initMasterPool(poolConfig,
                        new JedisFactory(uri, Protocol.DEFAULT_TIMEOUT, Protocol.DEFAULT_TIMEOUT, null) {});
            } else {
                JedisPool slave = new JedisPool(poolConfig, uri);
                HostAndPort hostAndPort = new HostAndPort(uri.getHost(), uri.getPort());
                slaveAddrs.add(hostAndPort);
                slavePools.put(hostAndPort, slave);
            }
        }
        if (!hasMaster) {
            destroySlavePools();
            throw new IllegalStateException("No master redis was detected");
        }
        log.info("Total {} slaves are detected", slavePools.size());
    }

    public JedisMasterSlavePool(GenericObjectPoolConfig<Jedis> poolConfig, Set<HostAndPort> hostAndPorts, String password, int db) {
        boolean hasMaster = false;
        for (HostAndPort hap : hostAndPorts) {
            try (Jedis jedis = new Jedis(hap)){
                if (StringUtils.isNotBlank(password)) {
                    jedis.auth(password);
                }
                JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                        .password(password)
                        .database(db).build();
                if (isMaster(jedis)) {
                    hasMaster = true;
                    initMasterPool(poolConfig, new JedisFactory(hap, clientConfig) {});
                } else {
                    JedisPool slave = new JedisPool(poolConfig, hap, clientConfig);
                    HostAndPort slaveHap = new HostAndPort(hap.getHost(), hap.getPort());
                    slaveAddrs.add(slaveHap);
                    slavePools.put(slaveHap, slave);
                }
            } catch (Exception e) {
                log.error("/category:redis/error_name:redis-error while detect redis {}", hap, e);
            }
        }
        if (!hasMaster) {
            destroySlavePools();
            throw new IllegalStateException("No master redis was detected");
        }
        log.info("Total {} slaves are detected", slavePools.size());
    }

    private boolean isMaster(URI uri) {
        try (Jedis jedis = new Jedis(uri)){
            return isMaster(jedis);
        } catch (Exception e) {
            log.error("/category:redis/error_name:redis-error occurred while determining the role of redis {}", uri, e);
        }
        return false;
    }

    private boolean isMaster(Jedis jedis) {
        List<Object> roleInfo = jedis.role();
        if (CollectionUtils.isEmpty(roleInfo)) {
            //could not determine the role
            log.warn("/category:redis/warn_name:redis-could not determine the redis role of host {} and port {}",
                    jedis.getClient().getHost(), jedis.getClient().getPort());
            return false;
        }
        return StringUtils.equals("master", String.valueOf(roleInfo.get(0)));
    }

    private void initMasterPool(GenericObjectPoolConfig<Jedis> poolConfig, PooledObjectFactory<Jedis> jedisFactory) {
        initPool(poolConfig, jedisFactory);
    }



    /**
     * Get a Jedis resource from master pool
     * @return Jedis
     */
    @Override
    public Jedis getResource() {
        return getResource(true);
    }

    public Jedis getResource(boolean master) {
        //获取主连接
        if (master || CollectionUtils.isEmpty(slaveAddrs)) {
            Jedis jedis = super.getResource();
            jedis.setDataSource(this);
            return jedis;
        }
        //获取备连接,尝试三次
        Jedis jedis = null;
        int retry = 0;
        while (null == jedis) {
            HostAndPort hap = slaveAddrs.get(RandomUtils.nextInt(0, slaveAddrs.size()));
            JedisPool pool = slavePools.get(slaveAddrs);
            if (null == pool) {
                throw new IllegalStateException("No proper pool for redis " + Objects.toString(hap));
            }
            retry++;
            try {
                jedis = pool.getResource();
                jedis.setDataSource(this);
                //add the slave pool associated to jedis in order to process the returning of jedis to pool
                allocatedMap.put(jedis, pool);
                return jedis;
            } catch (NoSuchElementException e) {
                if (retry > 2) {
                    if (null == e.getCause()) {
                        throw new JedisExhaustedPoolException("Could not get a resource since the pool is exhausted", e);
                    } else {
                        throw new JedisException("Could not get a resource from the pool", e);
                    }
                }
            } catch (Exception e) {
                if (retry > 2) {
                    throw new JedisConnectionException("Could not get a resource from the pool", e);
                }
            }
        }
        jedis = super.getResource();
        jedis.setDataSource(this);
        return jedis;
    }

    @Override
    public void returnResource(Jedis resource) {
        if (null != resource) {
            Pool<Jedis> pool = allocatedMap.remove(resource);
            if (null != pool) {
                //return resource to slave pool
                pool.returnResource(resource);
            } else {
                //return resource to master pool
                super.returnResource(resource);
            }
        }
    }

    @Override
    public void returnBrokenResource(Jedis resource) {
        if (null != resource) {
            Pool<Jedis> pool = allocatedMap.remove(resource);
            if (null != pool) {
                //return broken resource to slave pool
                pool.returnBrokenResource(resource);
            } else {
                //return broken resource to master pool
                super.returnBrokenResource(resource);
            }
        }
    }

    @Override
    public void destroy() {
        //close the master pool
        super.destroy();
        //close the slave pool
        destroySlavePools();
    }

    public void destroySlavePools() {
        //close the slave pool
        for ( Map.Entry<HostAndPort, JedisPool> entry: slavePools.entrySet()) {
            try {
                entry.getValue().close();
            } catch (Exception e) {
                log.warn("/category:redis/warn_name:redis-could not close jedis pool {}", entry.getKey(), e);
            }
        }
    }
}

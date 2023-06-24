package com.plato.recoserver.recoserver.datamanager.dao.redis;

import com.aliyun.tair.tairbloom.TairBloom;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author
 * @date
 */
@Slf4j
public class JedisClient {

    private JedisMasterSlavePool masterSlavePool;

    public JedisClient(Set<URI> uris, GenericObjectPoolConfig<Jedis> poolConfig) {
        if (CollectionUtils.isEmpty(uris)) {
            throw new IllegalArgumentException("The redis uris is invalid");
        }
        masterSlavePool = new JedisMasterSlavePool(poolConfig, uris);
    }

    public JedisClient(Set<HostAndPort> hostPorts, String password, int db, GenericObjectPoolConfig<Jedis> poolConfig) {
        if (CollectionUtils.isEmpty(hostPorts)) {
            throw new IllegalArgumentException("The redis address is invalid");
        }
        masterSlavePool = new JedisMasterSlavePool(poolConfig, hostPorts, password, db);
    }

    private Jedis getJedis(boolean master) {
        try {
            return masterSlavePool.getResource(master);
        } catch (Exception e) {
            log.error("There is an error while get jedis from pool", e);
        }
        return null;
    }

    /**
     * Retrieve a String value from redis with the specified key
     * @param key the key to find
     * @return the value associated the key
     */
    public String get(final String key) {
        return execute(jedis -> jedis.get(key));
    }

    /**
     * set the {@code key} with specified value
     * @param key
     * @param value
     * @return the value set to the key
     */
    public String set(final String key, final String value) {
        return execute(jedis -> jedis.set(key, value));
    }


    public <T> T execute(Function<Jedis, T> function) {
        return execute(function, null);
    }

    public <T> T execute(Function<Jedis, T> function, T defValue) {
        try (Jedis jedis = getJedis(true)){
            if (null == jedis) {
                return defValue;
            }
            return function.apply(jedis);
        }
    }

    /**
     * execute from slave redis
     * @param function
     * @param <T>
     * @return
     */
    public <T> T executeR(Function<Jedis, T> function) {
        return executeR(function, null);
    }

    public <T> T executeR(Function<Jedis, T> function, T defValue) {
        try (Jedis jedis = getJedis(false)){
            if (null == jedis) {
                return defValue;
            }
            return function.apply(jedis);
        }
    }

    public String bfreserve(final String key, final long initCapacity, final double errorRate) {
        return executeBloom(tairBloom -> tairBloom.bfreserve(key, initCapacity, errorRate));
    }

    public <T> T executeBloom(Function<TairBloom, T> function) {
        return executeBloom(function, null);
    }

    public <T> T executeBloom(Function<TairBloom, T> function, T defValue) {
        try (Jedis jedis = getJedis(true)){
            if (null == jedis) {
                return defValue;
            }
            TairBloom tairBloom = new TairBloom(jedis);
            return function.apply(tairBloom);
        }
    }

    public <T> T executeBloomR(Function<TairBloom, T> function) {
        return executeBloomR(function, null);
    }

    public <T> T executeBloomR(Function<TairBloom, T> function, T defValue) {
        try (Jedis jedis = getJedis(false)){
            if (null == jedis) {
                return defValue;
            }
            TairBloom tairBloom = new TairBloom(jedis);
            return function.apply(tairBloom);
        }
    }

    public <T, R> List<Response<R>> executePipeline(Collection<T> keys, BiFunction<Pipeline, T, Response<R>> function) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        try (Jedis jedis = getJedis(true)){
            if (null == jedis) {
                return Collections.emptyList();
            }
            List<Response<R>> result = Lists.newArrayListWithCapacity(keys.size());
            Pipeline pipeline = jedis.pipelined();
            for (T k : keys) {
                result.add(function.apply(pipeline, k));
            }
            pipeline.sync();
            return result;
        }
    }

    public <T, R> List<Response<R>> executeRPipeline(Collection<T> keys, BiFunction<Pipeline, T, Response<R>> function) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        try (Jedis jedis = getJedis(false)){
            if (null == jedis) {
                return Collections.emptyList();
            }
            List<Response<R>> result = Lists.newArrayListWithCapacity(keys.size());
            Pipeline pipeline = jedis.pipelined();
            for (T k : keys) {
                result.add(function.apply(pipeline, k));
            }
            pipeline.sync();
            return result;
        }
    }


    public static void main(String[] args) {
        Jedis jedis = new Jedis("r-2zem0286ielmbanc83pd.redis.rds.aliyuncs.com", 6379);
        jedis.auth("^!jmc7Y3B91kFdIN");
        System.out.println(jedis.role());
    }
}

package com.plato.recoserver.recoserver.datamanager.dao.redis;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.FactoryBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.util.JedisURIHelper;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @date 2022-03-07
 */
@Getter
@Setter
public class JedisClientFactoryBean implements FactoryBean<JedisClient> {

    private Set<String> uri;

    private Set<String> host;

    private String password;

    private int db;

    private JedisPoolConfig poolConfig = new JedisPoolConfig();

    @Override
    public JedisClient getObject() throws Exception {
        if (CollectionUtils.isNotEmpty(uri)) {
            Set<URI> uriSet = uri.stream()
                    .map(URI::create)
                    .filter(JedisURIHelper::isValid)
                    .collect(Collectors.toSet());
            return new JedisClient(uriSet, poolConfig);
        }
        if (CollectionUtils.isEmpty(host)) {
            throw new IllegalArgumentException("No available redis host configuration");
        }
        Set<HostAndPort> hostAndPorts = host.stream()
                .map(e -> e.split(":"))
                .filter(e -> e.length == 2)
                .map(e -> new HostAndPort(e[0], Integer.parseInt(e[1])))
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(hostAndPorts)) {
            throw new IllegalArgumentException("No available redis host configuration");
        }
        return new JedisClient(hostAndPorts, password, db, poolConfig);
    }

    @Override
    public Class<?> getObjectType() {
        return JedisClient.class;
    }
}

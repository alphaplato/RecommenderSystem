package com.plato.recoserver.recoserver.configure;

import com.alibaba.hologres.client.HoloClient;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.core.retrieval.HoloRetrievalItemProvider;
import com.plato.recoserver.recoserver.core.retrieval.ModelRetrievalItemProvider;
import com.plato.recoserver.recoserver.core.retrieval.RedisRetrievalItemProvider;
import com.plato.recoserver.recoserver.core.retrieval.inf.IRetrievalDataProvider;
import com.plato.recoserver.recoserver.datamanager.dao.hologres.HoloClientFactoryBean;
import com.plato.recoserver.recoserver.datamanager.dao.redis.JedisClient;
import com.plato.recoserver.recoserver.datamanager.dao.redis.JedisClientFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * springboot应用程序配置类
 * @author Kevin
 * @date 2022-03-03
 */
@Configuration
public class RecoServerConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "redis.client")
    @ConditionalOnExpression("'${redis.client.host:}'.length() > 0 " +
            "&& '${redis.client.retrieval.host:}'.length() == 0 " +
            "&& '${redis.client.profile.host:}'.length() == 0 " +
            "&& '${redis.client.history.host:}'.length() == 0 " +
            "&& '${redis.client.tairbloom.host:}'.length() == 0")
    public JedisClientFactoryBean jedisClient(){
        return new JedisClientFactoryBean();
    }

    @Bean
    @ConfigurationProperties(prefix = "redis.client.retrieval")
    @ConditionalOnExpression("'${redis.client.retrieval.host:}'.length() > 0")
    public JedisClientFactoryBean retrievalJedis(){
        return new JedisClientFactoryBean();
    }

    @Bean
    @ConfigurationProperties(prefix = "redis.client.history")
    @ConditionalOnExpression("'${redis.client.history.host:}'.length() > 0")
    public JedisClientFactoryBean historyJedis(){
        return new JedisClientFactoryBean();
    }

    @Bean
    @ConfigurationProperties(prefix = "redis.client.tairbloom")
    @ConditionalOnExpression("'${redis.client.tairbloom.host:}'.length() > 0")
    public JedisClientFactoryBean tairbloomJedis(){
        return new JedisClientFactoryBean();
    }

    @Bean
    @ConfigurationProperties(prefix = "hologres.client.profile")
    @ConditionalOnExpression("'${hologres.client.profile.url:}'.length() > 0")
    public HoloClientFactoryBean holoClient(){
        return new HoloClientFactoryBean();
    }

    @Bean(name = "redisProvider")
    public IRetrievalDataProvider<CandidateItem> redisRetrievalItemProvider(JedisClient retrievalJedis) {
        return new RedisRetrievalItemProvider(retrievalJedis);
    }

    @Bean(name = "holoProvider")
    public IRetrievalDataProvider<CandidateItem> holoRetrievalItemProvider(HoloClient retrievalHolo) {
        return new HoloRetrievalItemProvider(retrievalHolo);
    }

    @Bean(name = "modelProvider")
    public IRetrievalDataProvider<CandidateItem> modelRetrievalItemProvider(HoloClient retrievalHolo) {
        return new ModelRetrievalItemProvider(retrievalHolo);
    }


    @Bean
    public ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setQueueCapacity(10);
        executor.setMaxPoolSize(40);
        executor.setKeepAliveSeconds((int)TimeUnit.MINUTES.toSeconds(1L));
        executor.setThreadNamePrefix("async-task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor cxtBuilderExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setQueueCapacity(20);
        executor.setMaxPoolSize(60);
        executor.setKeepAliveSeconds((int)TimeUnit.MINUTES.toSeconds(1L));
        executor.setThreadNamePrefix("ctx-builder-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor retrievalExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(40);
        executor.setQueueCapacity(40);
        executor.setMaxPoolSize(80);
        executor.setKeepAliveSeconds((int)TimeUnit.MINUTES.toSeconds(1L));
        executor.setThreadNamePrefix("retrieval-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor tairBloomExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setQueueCapacity(20);
        executor.setMaxPoolSize(60);
        executor.setKeepAliveSeconds((int)TimeUnit.MINUTES.toSeconds(1L));
        executor.setThreadNamePrefix("tair-bloom-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
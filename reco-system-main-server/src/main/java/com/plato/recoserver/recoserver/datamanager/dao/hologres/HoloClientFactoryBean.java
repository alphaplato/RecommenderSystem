package com.plato.recoserver.recoserver.datamanager.dao.hologres;

import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.client.HoloConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author lishuguang
 * @date 2022-06-07
 */
@Getter
@Setter
public class HoloClientFactoryBean implements FactoryBean<HoloClient> {

    private String url;

    private String username;

    private String password;

    private int threadsize;

    private int batchsize;

    private int queuesize;

    @Override
    public HoloClient getObject() throws Exception {
        HoloConfig config = new HoloConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setReadThreadSize(threadsize);
        config.setReadBatchSize(batchsize);
        config.setReadBatchQueueSize(queuesize);
        config.setMetaCacheTTL(600000);
        config.setConnectionMaxIdleMs(6000);

        return new HoloClient(config);
    }

    @Override
    public Class<?> getObjectType() {
        return HoloClient.class;
    }
}

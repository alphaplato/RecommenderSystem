package com.plato.recoserver.recoserver.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.google.common.collect.Maps;
import com.plato.recoserver.recoserver.core.retrieval.BasedRetrieval;
import com.plato.recoserver.recoserver.core.retrieval.RetrievalConfig;
import com.plato.recoserver.recoserver.core.retrieval.inf.AbstractRetrieval;
import com.plato.recoserver.recoserver.core.retrieval.inf.IRetrieval;
import com.plato.recoserver.recoserver.core.retrieval.inf.IRetrievalDataProvider;
import com.plato.recoserver.recoserver.util.RecConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author Kevin
 * @date 2022-03-15
 */
@Component
@Slf4j
public class RecoServerAppContext {

    @Autowired
    private ApplicationContext applicationContext;

    @ApolloJsonValue("${reco.server.retrieval.config:{}}")
    private Map<String, RetrievalConfig> retrievalConfigs;

    private Map<String, IRetrieval> allRetrievals = Maps.newHashMap();

    @PostConstruct
    public void init() throws Exception {
       reloadRetrieval(retrievalConfigs);
    }

    public IRetrieval getRetrieval(String name) {
        return allRetrievals.get(name);
    }


    @ApolloConfigChangeListener(interestedKeys = RecConstants.RETRIEVAL_CONFIGS_KEY)
    private void retrievalConfigChange(ConfigChangeEvent event) {
        ConfigChange change = event.getChange(RecConstants.RETRIEVAL_CONFIGS_KEY);
        if (null == change) {
            return;
        }
        try {
            Map<String, RetrievalConfig> configMap =
                    JSON.parseObject(change.getNewValue(), new TypeReference<Map<String, RetrievalConfig>>(){});
            if (configMap.size() == 0) {
                log.warn("/category:context/warn_name:context-new retrieval config contains empty retrieval/config_to_check:{}",
                        change.getNewValue());
                return;
            }
            reloadRetrieval(configMap);
            log.info("Reload retrieval config successfully with {} retrievals, config: {}",
                    configMap.size(), change.getNewValue());
        } catch (Exception e) {
            log.error("/category:context/error_name:context-exception parse the changed retrieval configs: {}, please check", change.getNewValue(), e);
        }

    }

    private void reloadRetrieval(Map<String, RetrievalConfig> configMap) throws Exception{
        for (Map.Entry<String, RetrievalConfig> entry : configMap.entrySet()) {
            RetrievalConfig config = entry.getValue();
            if (config.getName() == null) {
                config.setName(entry.getKey());
            }
            IRetrieval retrieval = new BasedRetrieval(config);
            if (retrieval instanceof AbstractRetrieval) {
                try {
                    IRetrievalDataProvider dataProvider =
                            applicationContext.getBean(config.getDataProvider(), IRetrievalDataProvider.class);
                    ((AbstractRetrieval)retrieval).setDataProvider(dataProvider);
                    allRetrievals.put(entry.getKey(), retrieval);
                    log.info("Set retrieval bean successfully with {} retrieval, data provider: {}",
                            entry.getKey(), config.getDataProvider());
                } catch (Exception e) {
                    log.error("Set retrieval bean failed with {} retrieval, data provider: {}",
                            entry.getKey(), config.getDataProvider());
                }
            }
        }
    }
}

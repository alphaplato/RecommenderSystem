package com.plato.recoserver.recoserver.context;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Kevin
 * @date 2022-04-20
 */
@Component
@Slf4j
public class CloudConfigChangeListener {

    @Autowired
    private RefreshScope refreshScope;

    @ApolloConfigChangeListener(interestedKeyPrefixes = "reco.server.model.configs")
    private void onChange(ConfigChangeEvent event) {
        refreshScope.refresh("featureExtractor");
        log.info("reload feature-extractor configuration for key {} done", event.changedKeys());
    }
}

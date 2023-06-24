package com.plato.recoserver.recoserver.core.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.plato.recoserver.recoserver.util.RecConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lishuguang
 * @date 2022/6/29
 **/
@Component
@Slf4j
@Setter
@Getter
public class RecommendConfiguration {
    /**
     * 1、存放系统使用的配置参数
     * 2、主要通过 apollo 注入
     **/

    // 1.retrieval configure

    @ApolloJsonValue("${reco.server.retrieval.channels:[]}")
    private List<ChannelConf> channelConfs;
    private Map<String, JSONObject> retrievalModelMap = new HashMap<>();
    @ApolloJsonValue("${reco.server.retrieval.trigger:{}}")
    private TriggerConf triggerConf;

    // 2.rank configure
    @ApolloJsonValue("${reco.server.model.configs}")
    private JSONObject modelConfigs;

    // 3.profile server configure
    @ApolloJsonValue("${reco.server.profile.rt_user_conf_scene:{}}")
    private JSONObject rtProfileConf;

    // 4.dependency class
    @Getter
    @Setter
    public static class ChannelConf {
        private String name;
        private int quota;
        private float priority;
    }
    @Getter
    @Setter
    public static class RetrievalConf {
        private String name;
        private String keyPrefix;
        private String keyValue;
        private String srcTable;
        private String desTable;
        private String extendInfo;
        private String dataProvider;
        private String srcModel;
    }

    @Getter
    @Setter
    public static class TriggerConf {
        private Integer diversity = 0;
        private Integer length = 0;
    }
    // 5.system function
    @ApolloConfig("model")
    private Config modelSpaceConfig;
    @PostConstruct
    public void init() {
        modelSpaceConfig.getPropertyNames().stream()
                .filter(e -> StringUtils.startsWith(e, RecConstants.MODEL_KEY_PREFIX) && e.length() > RecConstants.MODEL_KEY_PREFIX.length())
                .forEach(e -> {
                            String  k = StringUtils.substring(e, RecConstants.MODEL_KEY_PREFIX.length());
                            JSONObject v = JSON.parseObject(modelSpaceConfig.getProperty(e,""));
                            retrievalModelMap.put(k, v);
                });
        log.info("/category:configure/system_name:model initialized with keys {} in apollo space of model", retrievalModelMap.keySet());
    }

    // listen to get new model information
    @ApolloConfigChangeListener(value = "model", interestedKeyPrefixes = RecConstants.MODEL_KEY_PREFIX)
    private void modelChange(ConfigChangeEvent event) {
        for (String key : event.changedKeys()) {
            if (RecConstants.MODEL_KEY_PREFIX.length() >= key.length() || !StringUtils.startsWith(key, RecConstants.MODEL_KEY_PREFIX)) {
                //invalid changed key
                log.warn("/category:configure/warn_name:model invalidly changed with key {} in apollo space of model", key);
                continue;
            }
            ConfigChange change = event.getChange(key);
            if (StringUtils.isBlank(change.getNewValue())) {
                String modelName = StringUtils.substring(key, RecConstants.MODEL_KEY_PREFIX.length());
                retrievalModelMap.remove(modelName);
            } else {
                log.info("/category:configure/system_name:model changed with keys {} in apollo space of model", retrievalModelMap.keySet());
                retrievalModelMap.put(key, JSON.parseObject(change.getNewValue()));
            }
        }
    }
}

package com.plato.recoserver.recoserver.core.abtest;

import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author lishuguang
 * @date 2022/02/07
 */
@Component
@Slf4j
public class ABTestManager {

    private static final String ABTEST_KEY_PREFIX = "reco.server.abtest.";

    @ApolloConfig("experiment")
    private Config abtestConfig;

    private static final int BUCKET_TOTAL_NUM = 100;

    @Getter
    private Map<String, Map<Integer, ABTestProperty.Property>> flowToABProperty = Maps.newHashMap();

    @PostConstruct
    public void init() {
        abtestConfig.getPropertyNames().stream()
                .filter(e -> StringUtils.startsWith(e, ABTEST_KEY_PREFIX) && e.length() > ABTEST_KEY_PREFIX.length())
                .forEach(e -> initFlowToABProperty(e, abtestConfig.getProperty(e, "")));
    }

    public ABTestProperty getABProperties(RecommendContext context) {
        ABTestProperty abTestProperty = new ABTestProperty();
        Map<String, ABTestProperty.Property> abProperties = Maps.newHashMap();
        if(MapUtils.isEmpty(flowToABProperty)) {
            abTestProperty.setPropertymap(abProperties);
            return abTestProperty;
        };
        RecoRequest request = context.getRequest();
        for (Map.Entry<String, Map<Integer, ABTestProperty.Property>> entry : flowToABProperty.entrySet()){
            String key =  entry.getKey();
            int hash = getBucketNo(request.getDeviceId(), key);
            ABTestProperty.Property property = entry.getValue().get(hash);
            abProperties.put(key, property);
        }
        abTestProperty.setPropertymap(abProperties);
        return abTestProperty;
    }

    private void initFlowToABProperty(String key, String property) {
        if (StringUtils.isBlank(property)) {
            log.warn("/category:ab_test/warn_name:ab_test-empty abtest property for key {}", key);
            return;
        }
        try {
            String abName = StringUtils.substring(key, ABTEST_KEY_PREFIX.length());
            List<ABTestProperty.Property> abTestProperties = JSON.parseArray(property, ABTestProperty.Property.class)
                    .stream()
                    .limit(10)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (CollectionUtils.isEmpty(abTestProperties)) {
                log.warn("/category:ab_test/warn_name:ab_test-cannot parse any plan for key {}, value {}", key, property);
                return;
            }
            Map<Integer, ABTestProperty.Property> planMap = Maps.newHashMap();
            int begin = 0;
            for (ABTestProperty.Property plan : abTestProperties) {
                Range<Integer> range = Range.closedOpen(begin, begin + plan.getFlow());
                ContiguousSet.create(range, DiscreteDomain.integers()).forEach(e->{
                    planMap.put(e,plan);
                });
                begin = begin + plan.getFlow();
            }
            if (begin < BUCKET_TOTAL_NUM) {
                Range<Integer> range = Range.closedOpen(begin, BUCKET_TOTAL_NUM);
                ContiguousSet.create(range, DiscreteDomain.integers()).forEach(e -> {
                    ABTestProperty.Property abDefault = new ABTestProperty.Property();
                    abDefault.setGroup("default");
                    planMap.put(e, abDefault);
                });
            }
            if (MapUtils.isNotEmpty(planMap)) {
                flowToABProperty.put(abName, planMap);
            }
            log.info("loaded {} abtest branch configuration for {}", abTestProperties.size(), key);
        } catch (Exception e) {
            log.error("/category:ab_test/error_name:ab_test-error while parsing ab plans of for key {}, value:{}", key, property, e);
        }

    }

    /**
     * 分流策略(计算流量对应分桶的编号)
     *
     * @param paramId 参数Id: 流量的标识
     * @param layerId 分层Id: 可为实验名称
     * @return 流量对应分桶编号.
     */
    public static int getBucketNo(String paramId, String layerId) {
        // 计算 MD5
        String destKey = (StringUtils.isNotBlank(paramId) ? paramId : "") + (StringUtils.isNotBlank(layerId) ? layerId : "");
        String md5Hex = DigestUtils.md5Hex(destKey);

        long hash = Long.parseUnsignedLong(md5Hex.substring(md5Hex.length() - 16, md5Hex.length() - 1), 16);
        if (hash < 0) {
            hash = hash * (-1);
        }
        // 取模
        return (int) (hash % BUCKET_TOTAL_NUM);
    }

    @ApolloConfigChangeListener(value = "experiment", interestedKeyPrefixes = ABTEST_KEY_PREFIX)
    private void abtestChange(ConfigChangeEvent event) {
        for (String key : event.changedKeys()) {
            if (ABTEST_KEY_PREFIX.length() >= key.length() || !StringUtils.startsWith(key, ABTEST_KEY_PREFIX)) {
                //invalid changed key
                log.warn("/category:ab_test/warn_name:ab_test-invalid changed key {} in namespace reco-abtest", key);
                continue;
            }
            ConfigChange change = event.getChange(key);
            if (StringUtils.isBlank(change.getNewValue())) {
                String abName = StringUtils.substring(key, ABTEST_KEY_PREFIX.length());
                flowToABProperty.remove(abName);
            } else {
                initFlowToABProperty(key, change.getNewValue());
            }
        }
    }
}

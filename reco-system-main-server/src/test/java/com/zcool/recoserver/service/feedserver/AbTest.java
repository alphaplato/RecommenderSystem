package com.zcool.recoserver.service.feedserver;

import com.google.common.collect.Maps;
import com.plato.recoserver.recoserver.configure.CloudConfigConfiguration;
import com.plato.recoserver.recoserver.core.abtest.ABTestManager;
import com.plato.recoserver.recoserver.core.abtest.ABTestProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;

/**
 * @author Kevin
 * @date 2022-04-22
 */
@SpringBootTest(properties = {
        "apollo.meta=http://apolloconfig-dev.in.zcool.cn/config",
//        "apollo.meta=http://apolloconfig-test.in.zcool.cn/config",
//        "apollo.meta=http://apolloconfig-prod.in.zcool.cn/config",
},classes = {ABTestManager.class, ABTestProperty.class})
@SpringJUnitConfig({CloudConfigConfiguration.class})
@DirtiesContext
@EnableConfigurationProperties
//@RunWith(SpringRunner.class)
@Slf4j
public class AbTest {
    @Autowired
    private ABTestManager abTestManager;

    @Test
    public void getABProperties() {
        StringBuilder tracking = new StringBuilder();
        String a="";
        tracking.append(a).toString();
        if(abTestManager.getFlowToABProperty().isEmpty()) return;
        Map<String, ABTestProperty.Property> abProperties = Maps.newHashMap();
        for (Map.Entry<String, Map<Integer, ABTestProperty.Property>> entry : abTestManager.getFlowToABProperty().entrySet()){
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue().keySet());
//            entry.getValue().values().stream().forEach(e->System.out.println(e.getGroup()));
//            entry.getValue().values().stream().forEach(e->System.out.println(e.getFlow()));
            String key =  entry.getKey();
            System.out.println(key);
            int hash = ABTestManager.getBucketNo("11111", key);
            ABTestProperty.Property property = entry.getValue().get(hash);
            abProperties.put(key, property);
            System.out.println(property.getGroup());
        }
        abProperties.get("gogoupcourse");
//        abProperties.values().stream().forEach(e->System.out.println(e.getGroup()));
        System.out.println(abProperties.get("gogoupcourse").getGroup());
        System.out.println(StringUtils.equals(abProperties.get("gogoupcourse").getGroup(), "gogoupcourse_b1"));

    }
}

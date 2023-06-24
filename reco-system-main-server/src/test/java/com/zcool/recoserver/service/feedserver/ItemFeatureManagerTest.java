package com.zcool.recoserver.service.feedserver;

import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.configure.CloudConfigConfiguration;
import com.plato.recoserver.recoserver.configure.RecoServerConfiguration;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureExtractor;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ItemFeatureManager;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishuguang
 * @date 2022/6/14
 **/
@SpringBootTest(properties = {
        "apollo.meta=http://apolloconfig-dev.in.zcool.cn/config",
        "hologres.client.profile.url=jdbc:postgresql://hgprecn-cn-2r42ppl1q001-cn-beijing.hologres.aliyuncs.com:80/profile_service",
        "hologres.client.profile.username=LTAI5tKwmYTF8EhrpgtTcdSt",
        "hologres.client.profile.password=bCNsmidzs9028bATP1CXZj5zn3PwI3",
        "hologres.client.profile.threadsize=2",
        "hologres.client.profile.batchsize=256",
        "hologres.client.profile.queuesize=128",
        "redis.client.host=r-2zem0286ielmbanc83pd.redis.rds.aliyuncs.com:6379",
        "redis.client.password=^!jmc7Y3B91kFdIN",
        "redis.client.db=0"
},classes = {
        ItemFeatureManager.class,FeatureExtractor.class
})
@SpringJUnitConfig({RecoServerConfiguration.class, CloudConfigConfiguration.class})
@DirtiesContext
@EnableConfigurationProperties
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@RunWith(SpringRunner.class)
@Component
class ItemFeatureManagerTest {
    @Autowired
    private FeatureExtractor featureExtractor;

    @Autowired
    private ItemFeatureManager itemFeatureManager;

    private List<Item> items = new ArrayList<>();
    @Test
    public void getItemProfiles() {
        String a="1223";
        System.out.println(String.valueOf(a));
        Item item = new CandidateItem(9290735L, 3,0.0);
        items.add(item);
        item = new CandidateItem(8728226L, 1,0.0);
        items.add(item);
        for (Item it : items){
            System.out.println(it.getId()+ "_" + it.type());
        }
//        itemFeatureManager.setItems(items);
//        Map<Item, Record> itemProfiles = itemFeatureManager.getItemFeature();
//        System.out.println(itemProfiles);
    }
}
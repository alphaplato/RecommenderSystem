package com.zcool.recoserver.service.feedserver;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.HoloClient;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.configure.CloudConfigConfiguration;
import com.plato.recoserver.recoserver.configure.RecoServerConfiguration;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ItemFeatureManager;
import com.plato.recoserver.recoserver.core.rerank.strategy.StrategyManger;
import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lishuguang
 * @date 2022/8/18
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
},classes = {ItemFeatureManager.class, StrategyManger.class}
)
@SpringJUnitConfig({RecoServerConfiguration.class, CloudConfigConfiguration.class})
@DirtiesContext
@EnableConfigurationProperties
@RunWith(SpringRunner.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class RerankerTest {
    @ApolloJsonValue("${reco.server.model.configs}")
    private JSONObject apolloValue;

    @ApolloJsonValue("${reco.server.profile.rt_user_conf}")
    private JSONObject profileConf;

    @Autowired
    StrategyManger strategyManger;

    @Autowired
    private HoloClient client;


    private List<Item> items = new ArrayList<>();


    @Test
    public void TestRerank() throws Exception {
//        TableSchema schema0 = client.getTableSchema("dws_feed_algo_profile_data_item_v3_4_3");
//        Scan scan = Scan.newBuilder(schema0)
////                .addEqualFilter("content_type", 1)
//                .withSelectedColumn("content_id")
//                .withSelectedColumn("content_type")
//                .setFetchSize(40)
//                .build();
//        items.add(new RetrievalItem(21111L,4,0.1));
//        items.add(new RetrievalItem(21131L,4,0.2));
//        items.add(new RetrievalItem(21511L,4,0.3));
//        //等同于select name, address from t0 where id=102 and name>=3 and name<4;
//        int size = 0;
//        try (RecordScanner rs = client.scan(scan)) {
//            while (rs.next() && size < 100) {
//                Record record = rs.getRecord();
//                Item item = new RetrievalItem(Long.valueOf(String.valueOf(record.getObject("content_id"))), Math.toIntExact(Long.parseLong(String.valueOf(record.getObject("content_type")))), Math.random());
//                items.add(item);
//                size ++;
//            }
//        }
//        String target = "cate";
//        Map<Item, Record> itemprofiles = itemFeatureManager.getItemProfiles(items, featureExtractor.getItemFeatureLists());
        System.out.println(strategyManger.getStrategies().size());
        System.out.println(strategyManger.getStrategies().stream().map(IStrategy::name).collect(Collectors.toList()));
    }
}
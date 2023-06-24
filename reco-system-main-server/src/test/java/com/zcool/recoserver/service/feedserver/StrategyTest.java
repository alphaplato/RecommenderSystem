package com.zcool.recoserver.service.feedserver;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.client.Scan;
import com.alibaba.hologres.client.model.Record;
import com.alibaba.hologres.client.model.RecordScanner;
import com.alibaba.hologres.client.model.TableSchema;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.configure.CloudConfigConfiguration;
import com.plato.recoserver.recoserver.configure.RecoServerConfiguration;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ItemFeatureManager;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.BreakInStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.ReWeightStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.ScatterStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.special.ArticleStrategy;
import lombok.Getter;
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

import java.util.*;
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
},classes = {ItemFeatureManager.class}
)
@SpringJUnitConfig({RecoServerConfiguration.class, CloudConfigConfiguration.class})
@DirtiesContext
@EnableConfigurationProperties
@RunWith(SpringRunner.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class StrategyTest {
    @ApolloJsonValue("${reco.server.model.configs}")
    private JSONObject apolloValue;

    @ApolloJsonValue("${reco.server.profile.rt_user_conf}")
    private JSONObject profileConf;

    @ApolloJsonValue("${reco.server.strategy.scatter:[]}")
    private List<ScatterStrategy> scatterStrategies;

    @ApolloJsonValue("${reco.server.strategy.supportRecomended3:[]}")
    private List<ReWeightStrategy> supportRecomendeds;

    @Getter
    @ApolloJsonValue("${reco.server.strategy.gogoupcourse:[]}")
    private List<BreakInStrategy> breakInStrategies ;

    @Getter
    @ApolloJsonValue("${reco.server.strategy.restrictions:[]}")
    private List<ArticleStrategy> restrictStrategies ;

    @Autowired
    private HoloClient client;

    private List<Item> items = new ArrayList<>();


    @Test
    public void testConf() {
        List<String> l = scatterStrategies.stream()
                .map(e->e.getPriority() + "_" + e.getWindowSize() + "_" + e.name() + "_" + e.getTarget()).collect(Collectors.toCollection(LinkedList::new));
        System.out.println(l);
    }

    @Test
    public void TestStrategy() throws Exception {
        TableSchema schema0 = client.getTableSchema("dws_feed_algo_profile_data_item_v3_1");
        Scan scan = Scan.newBuilder(schema0)
//                .addEqualFilter("content_type", 1)
                .withSelectedColumn("content_id")
                .withSelectedColumn("content_type")
                .withSelectedColumn("pid")
                .withSelectedColumn("item_recommend")
                .addEqualFilter("content_type","2")
                .setFetchSize(40)
                .build();
//

        Map<Item, Record> itemRecordMap = new HashMap<>();
        Map<String, String> itemStringMap2 = new HashMap<>();
        List<Record> records = new LinkedList<>();
        int size = 0;
        try (RecordScanner rs = client.scan(scan)) {
            while (rs.next() && size < 10) {
                Record record = rs.getRecord();
                records.add(record);
//                System.out.println(record.getObject("content_id").getClass());
                Item item = new CandidateItem(Long.valueOf(String.valueOf(record.getObject("content_id"))), Math.toIntExact(Long.parseLong(String.valueOf(record.getObject("content_type")))), Math.random());
                CandidateItem item3 = (CandidateItem) item;
                item3.setRankScore(0.01);
                item3.setSource("123");
                items.add(item);
                itemRecordMap.put(item, record);
                System.out.println(record.getObject("cate") == null);
                itemStringMap2.put(record.getObject("pid").toString(), record.getObject("item_recommend").toString());
                size++;
            }
        }

        items.add(new CandidateItem(2110011L, 4, 0.1));
        items.add(new CandidateItem(21131L, 4, 0.2));
        items.add(new CandidateItem(21511L, 4, 0.3));
        System.out.println(itemStringMap2);
//        System.out.println(supportRecomendeds.get(0).reWeight(items));
        System.out.println(restrictStrategies.get(0).restrict(items, itemRecordMap).stream().map(e->(e.type() + "_" + e.getId())).collect(Collectors.toList()));
//        Map<Item, Record> itemprofiles = itemFeatureManager.getItemProfiles(items, featureExtractor.getItemFeatureLists());
//            String target = "cate";
//            Map<Item, Record> itemprofiles = itemFeatureManager.getItemProfiles(items, featureExtractor.getItemFeatureLists());
//            System.out.println(itemprofiles.size());
//            ScatterStrategy strategy = new ScatterStrategy();
////        BreakInStrategy strategy1 = new BreakInStrategy();
//            strategy.setTarget(target);
//            strategy.setWindowSize(4);
//            List<?> l2 = strategy.scatter(itemprofiles, items).stream().map(e -> strategy.getObject(itemprofiles, e, target)).collect(Collectors.toCollection(LinkedList::new));
//        Set<?> l2 = items.stream().map(e->strategy.getObject(itemprofiles,e,target)).collect(Collectors.toCollection(HashSet::new));
//        System.out.println(l2);
//        strategy1.setPosition(1);
////        System.out.println(items.subList(0,10).stream().map(e->e.type() + "_" + e.getId()).collect(Collectors.toList()));
////        System.out.println(strategy1.breakIn(items.subList(0,10),items).stream().map(e->e.type() + "_" + e.getId()).collect(Collectors.toList()));
//        FolderStrategy strategy2 = new FolderStrategy();
//        strategy2.setRatio(0.01);
////        System.out.println(strategy2.restrict(items,2).stream().map(e->e.type() + "_" + e.getId()).collect(Collectors.toList()));

    }
}
package com.zcool.recoserver.service.feedserver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.Get;
import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.client.Scan;
import com.alibaba.hologres.client.SortKeys;
import com.alibaba.hologres.client.exception.HoloClientException;
import com.alibaba.hologres.client.model.Record;
import com.alibaba.hologres.client.model.RecordScanner;
import com.alibaba.hologres.client.model.TableSchema;
import com.alibaba.hologres.com.google.common.collect.Maps;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.google.common.base.Stopwatch;
import com.plato.recoserver.recoserver.SpringUtil;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.configure.CloudConfigConfiguration;
import com.plato.recoserver.recoserver.configure.RecoServerConfiguration;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureExtractor;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureProperty;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ContextFeatureManager;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ItemFeatureManager;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.UserFeatureManager;
import com.plato.recoserver.recoserver.core.retrieval.RetrievalConfig;
import com.zcool.recoserver.grpc.service.RecoRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.tensorflow.framework.TensorProto;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
},classes = {ItemFeatureManager.class, FeatureExtractor.class, UserFeatureManager.class, ContextFeatureManager.class, FeatureExtractor.class, SpringUtil.class}
)
@SpringJUnitConfig({RecoServerConfiguration.class, CloudConfigConfiguration.class})
@DirtiesContext
@EnableConfigurationProperties
@RunWith(SpringRunner.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
class FeatureExtractorTest {
    private final static String TABLE_NAME = "tableName";
    private final static String PRIMARY_KEY = "primaryKey";

    @ApolloJsonValue("${reco.server.model.configs}")
    private JSONObject apolloValue;

    @ApolloJsonValue("${reco.server.profile.user_conf}")
    private JSONObject profileConf;
    @ApolloJsonValue("${reco.server.profile.rt_user_conf}")
    private JSONObject rtProfileConf;

    @ApolloJsonValue("${reco.server.retrieval.config}")
    private Map<String, RetrievalConfig> retrievalConfs = Maps.newHashMap();

    private RetrievalConfig retrievalConfig;

    @Autowired
    private FeatureExtractor featureExtractor;

    @Autowired
    private HoloClient client;

    @ApolloJsonValue("${reco.server.model.configs}")
    private static JSONObject modelConfigs;

    private Map<String, FeatureProperty> propertyMap;

    private static final String NAME_ITEM_PROFILE = "item";
    private static final String NAME_USER_PROFILE = "user";
    private static final String NAME_CONTEXT = "context";

    private Record userProfile;
    private List<Record> rtUserProfile;


    private List<Item> items = new LinkedList<>();


    @Test
    public void getExtractors() {
        System.out.println(apolloValue.getClass());
        System.out.println(apolloValue.getJSONArray("feature_conf"));
        System.out.println(JSONArray.toJSONString(apolloValue.get("feature_conf")).getClass());
        System.out.println(JSONArray.toJSONString(apolloValue.get("feature_conf")));
        List<FeatureProperty> list = JSONObject.parseArray(JSONArray.toJSONString(apolloValue.get("feature_conf")), FeatureProperty.class);
        System.out.println(list);
        list.stream().forEach(e->{
            System.out.println(e);
        });
    }

    @Test
    public void buildAllFeatures() throws HoloClientException {
        testUserProfile();
        testRTUserProfile();
        RecoRequest recoRequest = RecoRequest.getDefaultInstance();
        RecommendContext context = new RecommendContext(recoRequest);

        context.setRtUserBehaviors(rtUserProfile);
        context.setUserProfile(userProfile);
        getItems();
        System.out.println(userProfile.getObject("max_click_cate"));
        System.out.println(rtUserProfile);
        System.out.println(userProfile);
        System.out.println(items);
        Map<String, TensorProto> predictFeature  = featureExtractor.FeatureBuilder(items, context, modelConfigs);
//        System.out.println(predictFeature);
    }



    @Test
    public void getItems() throws HoloClientException {
        TableSchema schema0 = client.getTableSchema("dws_feed_algo_profile_data_item_v3_8");
        Scan scan = Scan.newBuilder(schema0)
//                .addEqualFilter("content_type", 1)
                .withSelectedColumn("content_id")
                .withSelectedColumn("content_type")
                .setFetchSize(40)
                .build();
        //等同于select name, address from t0 where id=102 and name>=3 and name<4;
        int size = 0;
        List<String> temp = new ArrayList<>();
        try (RecordScanner rs = client.scan(scan)) {
            while (rs.next() && size < 40) {
                Record record = rs.getRecord();
//                System.out.println(record.getObject("content_id").getClass());
                Item item = new CandidateItem(Long.valueOf(String.valueOf(record.getObject("content_id"))), Integer.parseInt((String) record.getObject("content_type")), Math.random());
                items.add(item);
                temp.add("'1_" + String.valueOf(record.getObject("content_id")) + "'");
                size++;
            }
        }
    }

    @Test
    public void testUserProfile() {
        try {
            TableSchema schema = client.getTableSchema(profileConf.getString(TABLE_NAME));
            Get get = Get.newBuilder(schema).setPrimaryKey(profileConf.getString(PRIMARY_KEY), "24482604").build();
            userProfile = client.get(get).get();
        } catch (HoloClientException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRTUserProfile() throws HoloClientException {
       String uids  = "1001023";
//        String uids = "23206690";
        String[] uidArr = uids.split(",");
        List<String> uidList = Arrays.asList(uidArr);
        Collections.shuffle(uidList);
        TableSchema schema = client.getTableSchema(rtProfileConf.getString(TABLE_NAME));
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis() / 1000;
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 24);
        long startTime = calendar.getTimeInMillis() / 1000;
//        System.out.println(endTime-startTime);
        String[] selectedColumns = JSONObject
                .parseArray(rtProfileConf.getJSONArray("selectedColumns").toJSONString(), String.class)
                .toArray(new String[0]);
        List<Record> records = new LinkedList<>();
        for (String uid : uidList){
            Stopwatch sw = Stopwatch.createStarted();
//            System.out.println(uid);
            Scan scan = Scan.newBuilder(schema)
                    .withSelectedColumn("uid")
                    .withSelectedColumns(selectedColumns)
                    .addEqualFilter("uid", uid)
                    .addRangeFilter("time",startTime, endTime)
                    .setFetchSize(10000)
                    .setSortKeys(SortKeys.CLUSTERING_KEY)
                    .build();
            RecordScanner rs = client.scan(scan);
            while (rs.next()) {
                Record record = rs.getRecord();
                records.add(record);
            }
        }
        Collections.reverse(records);
        rtUserProfile = records;
//        System.out.println(rtUserProfile.get(0));
        List<String> rs = rtUserProfile.get(0).getBitSet().stream()
                .mapToObj(e->rtUserProfile.get(0).getSchema().getColumn(e).getName())
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
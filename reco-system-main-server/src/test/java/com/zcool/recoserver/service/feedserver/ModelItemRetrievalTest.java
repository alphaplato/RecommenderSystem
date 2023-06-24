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
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.plato.recoserver.recoserver.SpringUtil;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.configure.CloudConfigConfiguration;
import com.plato.recoserver.recoserver.configure.RecoServerConfiguration;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureExtractor;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureProperty;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ContextFeatureManager;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ItemFeatureManager;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.UserFeatureManager;
import com.plato.recoserver.recoserver.grpc.GrpcAuthInterceptor;
import com.plato.recoserver.grpc.service.RecoRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.lang3.StringUtils;
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
import tensorflow.serving.Model;
import tensorflow.serving.Predict;
import tensorflow.serving.PredictionServiceGrpc;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author lishuguang
 * @date 2022/11/21
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
        "redis.client.db=0",
        "reco.rank.ali-pai.auth.token=MzEwNzI0ZGNlZTM4MTIyMzQwNzRjZTBmNmIwYzBjMTVlMjMyZmU2Nw==",
        "grpc.client.ali-pai-retrieval.address=static://zcoolweb-reco-matching-multi-models.1703411985821693.cn-beijing.pai-eas.aliyuncs.com:80",
        "grpc.client.ali-pai-retrieval.enable-keep-alive=true",
        "grpc.client.ali-pai-retrieval.negotiation-type=plaintext"
},classes = {ItemFeatureManager.class, FeatureExtractor.class, UserFeatureManager.class, ContextFeatureManager.class, FeatureExtractor.class, SpringUtil.class}
)
@SpringJUnitConfig({RecoRpcTestConfiguration.class, RecoServerConfiguration.class, CloudConfigConfiguration.class})
@DirtiesContext
@EnableConfigurationProperties
@RunWith(SpringRunner.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
class ModelItemRetrievalTest {
    private final static String TABLE_NAME = "tableName";
    private final static String PRIMARY_KEY = "primaryKey";

    @ApolloJsonValue("${reco.server.profile.user_conf_matching_test}")
    private JSONObject profileConf;
    @ApolloJsonValue("${reco.server.profile.rt_user_conf_scene}")
    private JSONObject rtProfileConf;

    @Autowired
    private FeatureExtractor featureExtractor;

    @Autowired
    private HoloClient client;

    @ApolloJsonValue("${reco.server.ab.matchingYoutubednn.configs:{}}")
    private static JSONObject modelConfigs;

    private Record userProfile;
    private List<Record> rtUserProfile;

    @GrpcClient(value = "ali-pai-retrieval", interceptors = GrpcAuthInterceptor.class)
    private PredictionServiceGrpc.PredictionServiceBlockingStub predictionServiceBlockingStub;

    @Test
    public void getExtractors() {
        System.out.println(modelConfigs.getClass());
        System.out.println(modelConfigs.getJSONArray("feature_conf"));
        System.out.println(JSONArray.toJSONString(modelConfigs.get("feature_conf")).getClass());
        System.out.println(JSONArray.toJSONString(modelConfigs.get("feature_conf")));
        List<FeatureProperty> list = JSONObject.parseArray(JSONArray.toJSONString(modelConfigs.get("feature_conf")), FeatureProperty.class);
        System.out.println(list);
        list.stream().forEach(e -> {
            System.out.println(e);
        });
    }

    public void predict(String modelName,  Map<String, TensorProto> predictFeature){
        Model.ModelSpec.Builder modelSpecBuilder = Model.ModelSpec.newBuilder();
        modelSpecBuilder.setName(modelName);
        modelSpecBuilder.setSignatureName("serving_default");

        Predict.PredictRequest.Builder predictReqBuilder = Predict.PredictRequest.newBuilder();
        predictReqBuilder.setModelSpec(modelSpecBuilder);
        predictReqBuilder.putAllInputs(predictFeature);
        Predict.PredictResponse response = predictionServiceBlockingStub.predict(predictReqBuilder.build());
        Map<String, TensorProto> probs = response.getOutputsMap();
        //assign score to each item
        //itemScores = Maps.newHashMapWithExpectedSize(probs.getFloatValCount());
        ByteString pd = probs.get("user_emb").getStringVal(0);
        System.out.println(probs);
        System.out.println(pd.toStringUtf8());
        Object[] doubleValues = Arrays.stream(StringUtils.split(pd.toStringUtf8(), ","))
                .map(Double::valueOf).toArray();
//        Array a = (Array) doubleValues;
//        System.out.println(doubleValues.getClass());
    }


    @Test
    public void StringToVecTest(){
        String ss = "0.801662982,-0.92591399,1.38659298,-0.0645639971,0.761834979,2.9350059,2.65202999,-1.12416005,1.51975799,-1.32769704,3.08880711,0.246995002,-8.11196518,1.22607005,-0.327800006,-0.150718004,1.044945,-2.68903494,2.18137503,-1.77526796,2.12893796,-1.05014706,-1.08466494,0.553671002,-0.524370015,-0.603811026,0.87637502,0.129075006,-0.283813,2.61661506,-0.354878992,0.529011011";
        List<CandidateItem> candidates = Lists.newLinkedList();
        Object doubleValues = Arrays.stream(StringUtils.split(ss, ","))
                .map(Double::valueOf).toArray();
        try {
            client.sql(conn -> {
                String sql = String.format("select %s,pm_approx_inner_product_distance(embedding, ?) as distance from %s order by distance desc limit ?","item_id", "dssm_recall_item_embedding_holo_v1");
                PreparedStatement st = conn.prepareStatement(sql);
                Object[] o = (Object[]) doubleValues;
                Array array = conn.createArrayOf("FLOAT4", o);
                st.setArray(1, array);
                st.setInt(2, 100);
                System.out.println(st);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    String[] pid = rs.getString("item_id").split("_");
                    if(pid.length != 2) {
                        continue;
                    }
                    double score = rs.getDouble("distance");
                    candidates.add(new CandidateItem(Long.parseLong(pid[1]), Short.parseShort(pid[0]), score));
                }
                rs.close();
                return true;
            }).get();
        } catch (Exception e){
            String s = "";
        }
        System.out.println(candidates);
    }

    @Test
    public void buildAllFeatures() throws HoloClientException {
        testUserProfile();
        testRTUserProfile();
        RecoRequest recoRequest = RecoRequest.getDefaultInstance();
        RecommendContext context = new RecommendContext(recoRequest);

        context.setRtUserBehaviors(rtUserProfile);
        context.setUserProfile(userProfile);
//        System.out.println(userProfile.getObject("max_click_cate"));
//        System.out.println(rtUserProfile);
//        System.out.println(userProfile);
        String modelName = featureExtractor.getModelName(modelConfigs);
        Map<String, TensorProto> predictFeature = featureExtractor.FeatureBuilder(context, modelConfigs);
//        System.out.println(predictFeature);
        predict(modelName, predictFeature);
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
        String uids = "24482604";
        String[] uidArr = uids.split(",");
        List<String> uidList = Arrays.asList(uidArr);
        Collections.shuffle(uidList);
        TableSchema schema = client.getTableSchema(rtProfileConf.getString(TABLE_NAME));
        int duration = rtProfileConf.getIntValue("time");
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, -1 * duration);
        long startTime = calendar.getTimeInMillis();
//        System.out.println(endTime + "_" + startTime + "_" + duration);
        String[] selectedColumns = JSONObject
                .parseArray(rtProfileConf.getJSONArray("selectedColumns").toJSONString(), String.class)
                .toArray(new String[0]);
        List<Record> records = new LinkedList<>();
        for (String uid : uidList) {
            Stopwatch sw = Stopwatch.createStarted();
//            System.out.println(uid);
            Scan scan = Scan.newBuilder(schema)
                    .withSelectedColumn("uid")
                    .withSelectedColumns(selectedColumns)
                    .addEqualFilter("uid", uid)
                    .addRangeFilter("time", startTime, endTime)
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
                .mapToObj(e -> rtUserProfile.get(0).getSchema().getColumn(e).getName())
                .collect(Collectors.toCollection(LinkedList::new));
    }
}

package com.zcool.recoserver.service.feedserver;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.Get;
import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.client.Scan;
import com.alibaba.hologres.client.exception.HoloClientException;
import com.alibaba.hologres.client.model.Record;
import com.alibaba.hologres.client.model.RecordScanner;
import com.alibaba.hologres.client.model.TableSchema;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.configure.CloudConfigConfiguration;
import com.plato.recoserver.recoserver.configure.RecoServerConfiguration;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureExtractor;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureProperty;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ContextFeatureManager;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ItemFeatureManager;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.UserFeatureManager;
import com.plato.recoserver.recoserver.grpc.GrpcAuthInterceptor;
import com.zcool.recoserver.grpc.service.RecoRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.tensorflow.framework.DataType;
import org.tensorflow.framework.TensorProto;
import org.tensorflow.framework.TensorShapeProto;
import tensorflow.serving.Model;
import tensorflow.serving.Predict;
import tensorflow.serving.PredictionServiceGrpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Kevin
 * @date 2022-03-03
 */
@SpringBootTest(properties = {
        "apollo.meta=http://apolloconfig-dev.in.zcool.cn/config",
        "spring.application.name=Junit Test",
//        "grpc.client.test-rpc.address=static://127.0.0.1:7071",
//        "grpc.client.test-rpc.enable-keep-alive=true",
//        "grpc.client.test-rpc.negotiation-type=plaintext",
        "grpc.client.tensorflow-rank.address=static://zcool-reco-rank-multi-models.1703411985821693.cn-beijing.pai-eas.aliyuncs.com:80",
        "grpc.client.tensorflow-rank.enable-keep-alive=true",
        "grpc.client.tensorflow-rank.negotiation-type=plaintext",
        "reco.rank.ali-pai.auth.token=MzEwNzI0ZGNlZTM4MTIyMzQwNzRjZTBmNmIwYzBjMTVlMjMyZmU2Nw==",
        "hologres.client.profile.url=jdbc:postgresql://hgprecn-cn-2r42ppl1q001-cn-beijing.hologres.aliyuncs.com:80/profile_service",
        "hologres.client.profile.username=LTAI5tKwmYTF8EhrpgtTcdSt",
        "hologres.client.profile.password=bCNsmidzs9028bATP1CXZj5zn3PwI3",
        "hologres.client.profile.threadsize=50",
        "hologres.client.profile.batchsize=128",
        "hologres.client.profile.queuesize=64",
        "redis.client.host=r-2zem0286ielmbanc83pd.redis.rds.aliyuncs.com:6379",
        "redis.client.password=^!jmc7Y3B91kFdIN",
        "redis.client.db=0"
},classes = {ItemFeatureManager.class,FeatureExtractor.class})
@SpringJUnitConfig({RecoRpcTestConfiguration.class, CloudConfigConfiguration.class, RecoServerConfiguration.class})
@DirtiesContext
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class RecoRankServiceTest {
    @ApolloJsonValue("${reco.server.model.configs}")
    private JSONObject apolloValue;

    @GrpcClient(value = "tensorflow-rank", interceptors = GrpcAuthInterceptor.class)
    private PredictionServiceGrpc.PredictionServiceBlockingStub predictionServiceBlockingStub;

    @Autowired
    private FeatureExtractor featureExtractor;

    @Autowired
    private HoloClient client;

    @Autowired
    private UserFeatureManager userFeatureManager;

    @Autowired
    private ItemFeatureManager itemFeatureManager;

    @Autowired
    private ContextFeatureManager contextFeatureManager;

    private List<Item> items = new ArrayList<>();

    private Map<String, FeatureProperty> propertyMap;

    @ApolloJsonValue("${reco.server.model.configs}")
    private JSONObject modelConfigs;


    @Test
    @DirtiesContext
    public void grpcTest() throws Exception {
        Map<String, TensorProto> predictFeature = buildAllFeatures();
//        System.out.println(featureExtractor.getModelName());
//        System.out.println(predictFeature.size());
        System.out.println(predictFeature.keySet());
//        System.out.println(predictFeature);
        Model.ModelSpec.Builder modelSpecBuilder = Model.ModelSpec.newBuilder();
        modelSpecBuilder.setName(featureExtractor.getModelName(modelConfigs));
        modelSpecBuilder.setSignatureName("serving_default");
        Predict.PredictRequest.Builder predictReqBuilder = Predict.PredictRequest.newBuilder();
        predictReqBuilder.setModelSpec(modelSpecBuilder);
        predictReqBuilder.putAllInputs(predictFeature);
        //remote rank
        Predict.PredictResponse response = predictionServiceBlockingStub.predict(predictReqBuilder.build());
        TensorProto probs = response.getOutputsMap().get("probs");
        Map<Item, Double> itemScores = Maps.newHashMapWithExpectedSize(probs.getFloatValCount());

        System.out.println(response);
//        System.out.println(predictFeature);
    }

    public static TensorProto makeStringFeature(List<String> values) {
        TensorProto.Builder tpBuilder = TensorProto.newBuilder()
                .setTensorShape(TensorShapeProto.newBuilder()
                        .addDim(TensorShapeProto.Dim.newBuilder().setSize(values.size()))
                        .build()
                )
                .setDtype(DataType.DT_STRING);
        values.stream().map(ByteString::copyFromUtf8).forEach(tpBuilder::addStringVal);
        return tpBuilder.build();
    }

    public  Map<String, TensorProto> buildAllFeatures() throws HoloClientException {
        RecoRequest request = RecoRequest.getDefaultInstance();
        System.out.println(request);
        try {
            TableSchema schema = client.getTableSchema("dws_feed_algo_profile_data_user_new");
            Get get = Get.newBuilder(schema).setPrimaryKey("uid", "153884").build();
            client.get(get).get();


            TableSchema schema0 = client.getTableSchema("dws_feed_algo_profile_data_item_new");
            Scan scan = Scan.newBuilder(schema0)
                    .addEqualFilter("content_type", 1)
                    .withSelectedColumn("content_id")
                    .withSelectedColumn("content_type")
                    .setFetchSize(40)
                    .build();
            //等同于select name, address from t0 where id=102 and name>=3 and name<4;
            int size = 0;
            RecordScanner rs = client.scan(scan);
            while (rs.next() && size < 300) {
                    Record record = rs.getRecord();
//                System.out.println(record.getObject("content_id").getClass());
                Item item = new CandidateItem(Long.valueOf(String.valueOf(record.getObject("content_id"))), 1,0.0);
                items.add(item);
                size ++;
            }
            System.out.println(items.size());
        } catch (Exception e){
            return  Maps.newHashMap();
        }
//        while(true){
//            Map<Item, Record> itemprofiles = itemFeatureManager.getItemProfiles(items, featureExtractor.getItemFeatureLists());
//            featureExtractor.buildAllFeatures(request,user,itemprofiles.values());
//            Thread.sleep(5);
//        }
//        Map<Item, Record> itemprofiles = itemFeatureManager.getItemFeature(items);
//        return featureExtractor.buildAllFeatures(propertyMap, contextFeatureManager.getFeature(), userFeatureManager.getFeature(), itemprofiles.values());
        return null;
    }
}

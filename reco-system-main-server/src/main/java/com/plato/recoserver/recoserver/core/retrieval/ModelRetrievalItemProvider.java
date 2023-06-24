package com.plato.recoserver.recoserver.core.retrieval;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.org.postgresql.jdbc.PgArray;
import com.plato.recoserver.recoserver.core.context.RecommendConfiguration;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureExtractor;
import com.plato.recoserver.recoserver.grpc.GrpcAuthInterceptor;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.tensorflow.framework.TensorProto;
import tensorflow.serving.Model;
import tensorflow.serving.Predict;
import tensorflow.serving.PredictionServiceGrpc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author lishuguang
 * @date 2022/11/21
 **/
@Slf4j
public class ModelRetrievalItemProvider extends HoloRetrievalItemProvider{
    @GrpcClient(value = "ali-pai-retrieval", interceptors = GrpcAuthInterceptor.class)
    private PredictionServiceGrpc.PredictionServiceBlockingStub predictionServiceBlockingStub;
    @Autowired
    private FeatureExtractor featureExtractor;
    private static final String KEY_PROBS = "user_emb";

    public ModelRetrievalItemProvider(HoloClient holoClient) {
        super(holoClient);
    }

    @Override
    public Object[] getKeyEmbedding(String key, RecommendConfiguration.RetrievalConf retrievalConf){
        RecommendContext context = ((RetrievalConfig) retrievalConf).getRecommendContext();
        RecoRequest request = context.getRequest();
        JSONObject modelConfigs = context.getRecommendConfiguration().getRetrievalModelMap().get(retrievalConf.getSrcModel());

        if (MapUtils.isEmpty(modelConfigs)) {
            log.warn("/category:retrieval/warn_name:model config of retrieval is empty, would not do the remote inference/device_id:{}/user_id:{}/request_id:{}/model:{}/retrieval:{}",
                    request.getDeviceId(), request.getUserId(), request.getRequestId(), retrievalConf.getSrcModel(), retrievalConf.getName());
            return null;
        }

        String modelName = featureExtractor.getModelName(modelConfigs);
        Map<String, TensorProto> predictFeature  = featureExtractor.FeatureBuilder(context, modelConfigs);

        if (MapUtils.isEmpty(predictFeature)) {
            log.warn("/category:retrieval/warn_name:input feature of retrieval is empty, would not do the remote inference/device_id:{}/user_id:{}/request_id:{}/model:{}/retrieval:{}",
                    request.getDeviceId(), request.getUserId(), request.getRequestId(), retrievalConf.getSrcModel(), retrievalConf.getName());
            return null;
        }

        Model.ModelSpec.Builder modelSpecBuilder = Model.ModelSpec.newBuilder();
        modelSpecBuilder.setName(modelName);
        modelSpecBuilder.setSignatureName("serving_default");

        try {
            Predict.PredictRequest.Builder predictReqBuilder = Predict.PredictRequest.newBuilder();
            predictReqBuilder.setModelSpec(modelSpecBuilder);
            predictReqBuilder.putAllInputs(predictFeature);
            Predict.PredictResponse response = predictionServiceBlockingStub.predict(predictReqBuilder.build());
            TensorProto predict = response.getOutputsMap().get(KEY_PROBS);
            //assign score to each item
            //itemScores = Maps.newHashMapWithExpectedSize(probs.getFloatValCount());
            //itemScores = Maps.newHashMapWithExpectedSize(probs.getFloatValCount());
            if (predict.getStringValCount() > 0){
                return Arrays.stream(StringUtils.split(predict.getStringVal(0).toStringUtf8(), ","))
                        .map(Double::parseDouble)
                        .toArray();
            }
            return null;
        } catch (Exception e) {
            log.warn("/category:retrieval/warn_name:retrieval_invoke_remote_service_failed/device_id:{}/user_id:{}/request_id:{}/model_name:{}/exception:{}", request.getDeviceId(), request.getUserId(), request.getRequestId(), modelName,e);
        }
        return null;
    }

    @Override
    public PgArray convert(Object o, Connection conn) throws SQLException {
        Object[] p = (Object[]) o;
        return (PgArray) conn.createArrayOf("FLOAT4", p);
    }
}


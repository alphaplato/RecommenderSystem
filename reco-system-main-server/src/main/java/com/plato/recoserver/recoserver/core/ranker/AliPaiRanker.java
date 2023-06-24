package com.plato.recoserver.recoserver.core.ranker;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.model.Record;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.plato.recoserver.recoserver.core.abtest.ABTestConfiguration;
import com.plato.recoserver.recoserver.core.abtest.ABTestProperty;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureExtractor;
import com.plato.recoserver.recoserver.core.ranker.inf.IRanker;
import com.plato.recoserver.recoserver.grpc.GrpcAuthInterceptor;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tensorflow.framework.TensorProto;
import tensorflow.serving.Model;
import tensorflow.serving.Predict;
import tensorflow.serving.PredictionServiceGrpc;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @date 2022-03-21
 */
@Component
@Slf4j
public class AliPaiRanker implements IRanker {
    @GrpcClient(value = "ali-pai-rank", interceptors = GrpcAuthInterceptor.class)
    private PredictionServiceGrpc.PredictionServiceBlockingStub predictionServiceBlockingStub;
    @Autowired
    private FeatureExtractor featureExtractor;

    private static final String KEY_PROBS = "probs";

    /**
     * rank the item via a-li tf serving
     * @param context recommendation context
     * @param items the item list need to rank
     * @return item list order by score returned from a-li tf serving
     */
    @Override
    public List<Item> rank(RecommendContext context, List<Item> items) {
        Stopwatch swCollectingFeatures = Stopwatch.createStarted();
        RecoRequest request = context.getRequest();

        if (null == context.getUserProfile() && CollectionUtils.isEmpty(context.getRtUserBehaviors())) {
            log.warn("/category:rank/warn_name:rank-no user profile, skip the remote ranker/device_id:{}/user_id:{}/request_id:{}",
                    request.getDeviceId(), request.getUserId(), request.getRequestId());
            return items;
        }

        JSONObject modelConfigs = context.getRecommendConfiguration().getModelConfigs();

        // 排序 AB 实验
        ABTestConfiguration abTestConfiguration = context.getAbTestConfiguration();
        ABTestProperty abTestProperty = context.getAbTestProperty();
        if(abTestProperty.getPropertymap().get("rank_new_model") != null) {
            String group = abTestProperty.getPropertymap().get("rank_new_model").getGroup();
            if (StringUtils.equals(group, "tag_feature")){
                JSONObject abModelConfigs = abTestConfiguration.getModelConfigs();
                if (!abModelConfigs.isEmpty()) modelConfigs = abModelConfigs;
            } else if(StringUtils.equals(group, "incremental_train")){
                JSONObject abModelConfigs = abTestConfiguration.getModelConfigs2();
                if (!abModelConfigs.isEmpty()) modelConfigs = abModelConfigs;
            }
        }

        String modelName = featureExtractor.getModelName(modelConfigs);
        Map<String, TensorProto> predictFeature  = featureExtractor.FeatureBuilder(items, context, modelConfigs);
        Map<Item, Record> itemFeatures = context.getItemProfiles();

        log.info("/category:rank/input_size:{}/output_size:{}/not_find_size:{}/size_source:item_profiles/device_id:{}/user_id:{}/request_id:{}/model_name:{}",
                items.size(), CollectionUtils.size(itemFeatures), CollectionUtils.size(itemFeatures) - CollectionUtils.size(items),request.getDeviceId(), request.getUserId(), request.getRequestId(), modelName);

        if (MapUtils.isEmpty(predictFeature)) {
            log.warn("/category:rank/warn_name:rank-predict feature is empty, would do the remote rank/device_id:{}/user_id:{}/request_id:{}/event_id:{}",
                    request.getDeviceId(), request.getUserId(), request.getRequestId(), context.getEventId());
            return items;
        }
        log.info("/category:rank/size:{}/size_source:rank_predict_feature/cost_time_name:collecting_features/device_id:{}/user_id:{}/request_id:{}/cost_time:{}/predict_feature_key_set:{}",
                predictFeature.size(), request.getDeviceId(), request.getUserId(), request.getRequestId(), swCollectingFeatures.elapsed(TimeUnit.MILLISECONDS), predictFeature.keySet());

        // create a modelspec
        Stopwatch swInvokeRemoteService = Stopwatch.createStarted();
        // modelName update must sync with featureExtractor to keep consistency
        Model.ModelSpec.Builder modelSpecBuilder = Model.ModelSpec.newBuilder();
        modelSpecBuilder.setName(modelName);
        modelSpecBuilder.setSignatureName("serving_default");
        //remote rank
        Map<Item, Double> itemScores = Maps.newHashMapWithExpectedSize(MapUtils.size(predictFeature));
        try {
            Predict.PredictRequest.Builder predictReqBuilder = Predict.PredictRequest.newBuilder();
            predictReqBuilder.setModelSpec(modelSpecBuilder);
            predictReqBuilder.putAllInputs(predictFeature);
            Predict.PredictResponse response = predictionServiceBlockingStub.predict(predictReqBuilder.build());
            TensorProto probs = response.getOutputsMap().get(KEY_PROBS);
            //assign score to each item
//           itemScores = Maps.newHashMapWithExpectedSize(probs.getFloatValCount());
            int index = 0;
            Iterator<Item> iterator = itemFeatures.keySet().iterator();
            while (iterator.hasNext() && index < probs.getFloatValCount()) {
                itemScores.put(iterator.next(), (double)probs.getFloatVal(index++));
            }
        } catch (Exception e) {
            log.warn("/category:rank/warn_name:invoke_remote_service_failed/device_id:{}/user_id:{}/request_id:{}/model_name:{}/exception:{}", request.getDeviceId(), request.getUserId(), request.getRequestId(), modelName,e);
        }
        log.info("/category:rank/cost_time_name:invoke_remote_service/device_id:{}/user_id:{}/request_id:{}/cost_time:{}/model_name:{}",
                request.getDeviceId(), request.getUserId(), request.getRequestId(), swInvokeRemoteService.elapsed(TimeUnit.MILLISECONDS), modelName);
        //rank by score
        List<Item> rankedItems = itemScores.entrySet().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingDouble(Map.Entry::getValue)))
                .map(e->{
                    CandidateItem item = (CandidateItem) e.getKey();
                    item.setRankScore(e.getValue());
                    return item;
                })
                .collect(Collectors.toCollection(LinkedList::new));
        //append the items without remote ranking score to the tail of the ranked items
        items.stream()
                .filter(e -> !itemScores.containsKey(e))
                .forEach(rankedItems::add);
        log.info("/category:rank/item scores after iteration for user {} with id {}, request {} are {}",
                request.getDeviceId(), request.getUserId(), request.getRequestId(), rankedItems);
        return rankedItems;
    }

}

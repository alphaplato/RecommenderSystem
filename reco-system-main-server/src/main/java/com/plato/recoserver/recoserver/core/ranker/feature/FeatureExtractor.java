package com.plato.recoserver.recoserver.core.ranker.feature;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.model.Record;
import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ContextFeatureManager;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.ItemFeatureManager;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.UserFeatureManager;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tensorflow.framework.DataType;
import org.tensorflow.framework.TensorProto;
import org.tensorflow.framework.TensorShapeProto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @date 2022-04-06
 */
@Slf4j
@Component
public class FeatureExtractor {
    @Autowired
    private ItemFeatureManager itemFeatureManager;
    @Autowired
    private UserFeatureManager userFeatureManager;
    @Autowired
    private ContextFeatureManager contextFeatureManager;

    private static final String NAME_ITEM_PROFILE = "item";
    private static final String NAME_USER_PROFILE = "user";
    private static final String NAME_CONTEXT = "context";
//    private static final String NAME_COMBO = "combo";

    public String getModelName(JSONObject modelConfigs){
        if(modelConfigs.getJSONObject("model_conf") != null && modelConfigs.getJSONObject("model_conf").getString("model_name") != null) {
            return modelConfigs.getJSONObject("model_conf").getString("model_name");
        }
        return "model_default";
    }

    // 解析特征配置文件
    public Map<String, FeatureProperty> getPropertyMap(JSONObject modelConfigs){
        Map<String, FeatureProperty> propertyMap = Maps.newConcurrentMap();
        List<FeatureProperty> featureProperties = JSONObject.parseArray(JSONObject.toJSONString(modelConfigs.get("feature_conf")), FeatureProperty.class);
        featureProperties.parallelStream().filter(e -> e.getInputName() != null).forEach(e->propertyMap.put(e.getInputName(),e));
        return propertyMap;
    }

    public List<FeatureProperty> getFeatureProperties(String target, Map<String, FeatureProperty> propertyMap ){
        return propertyMap.values().stream()
                .filter(e->e.getGroup().equals(target) && e.getInputName() != null)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    // features of the user can be constructed for retrieval algorithm
    public Map<String, TensorProto> FeatureBuilder(RecommendContext context, JSONObject modelConfigs){
        // 初始化特征配置
        final Map<String, FeatureProperty> propertyMap =  getPropertyMap(modelConfigs);
        final Map<String, Object> contextFeatureMap = contextFeatureManager.getFeature(context, getFeatureProperties(NAME_CONTEXT, propertyMap));
        final Map<String, Object> userFeatureMap = userFeatureManager.getFeature(context, getFeatureProperties(NAME_USER_PROFILE, propertyMap));

        RecoRequest request = context.getRequest();
        if (MapUtils.isEmpty(contextFeatureMap) ||
                MapUtils.isEmpty(userFeatureMap) ||
                MapUtils.isEmpty(propertyMap)) {
            log.warn("/category:feature_manager/warn_name:at least one is empty in feature extractor/property_keys:{}/context_keys:{}/user_keys:{}/size_source:feature_manager/device_id:{}/user_id:{}/request_id:{}",
                    propertyMap.keySet(),contextFeatureMap.keySet(), userFeatureMap.keySet(), request.getDeviceId(), request.getUserId(), request.getRequestId());
            return Collections.emptyMap();
        }
        Map<String, Object> rawFeatureMap = Maps.newConcurrentMap();

        propertyMap.values()
                .parallelStream()
                .forEach(e -> {
                    rawFeatureMap.put(e.getInputName(), buildEachFeature(e ,contextFeatureMap, userFeatureMap, Collections.emptyList(), 1));
                });

        Map<String, TensorProto> predictFeatureMap = Maps.newConcurrentMap();
        rawFeatureMap.entrySet().
                parallelStream().
                forEach(e->predictFeatureMap.put(e.getKey(), toTensorProto(propertyMap.get(e.getKey()), e.getValue())));

//        log.info("/category:feature_manager/raw_features:{}/property_keys:{}/context_keys:{}/user_keys:{}/size_source:feature_manager/device_id:{}/user_id:{}/request_id:{}",
//                JSONObject.toJSONString(rawFeatureMap), propertyMap.keySet(), contextFeatureMap.keySet(), userFeatureMap.keySet(), request.getDeviceId(), request.getUserId(), request.getRequestId());

        log.info("/category:feature_manager/property_keys:{}/context_keys:{}/user_keys:{}/size_source:feature_manager/device_id:{}/user_id:{}/request_id:{}",
                propertyMap.keySet(), contextFeatureMap.keySet(), userFeatureMap.keySet(), request.getDeviceId(), request.getUserId(), request.getRequestId());


        return predictFeatureMap;
    }

    // features of candidate samples can be constructed for retrieval algorithm
    public Map<String, TensorProto> FeatureBuilder(List<Item> items, RecommendContext context, JSONObject modelConfigs){
        // 初始化特征配置
        final Map<String, FeatureProperty> propertyMap =  getPropertyMap(modelConfigs);
        final Map<String, Object> contextFeatureMap = contextFeatureManager.getFeature(context, getFeatureProperties(NAME_CONTEXT, propertyMap));
        final Map<String, Object> userFeatureMap = userFeatureManager.getFeature(context, getFeatureProperties(NAME_USER_PROFILE, propertyMap));
        final Map<Item, Record> itemFeatureMap = itemFeatureManager.getFeature(items, getFeatureProperties(NAME_ITEM_PROFILE, propertyMap));

        // 初始化 context 的内容画像
        context.setItemProfiles(itemFeatureMap);

        RecoRequest request = context.getRequest();
        final List<Record> itemFeatureList = new LinkedList<>(itemFeatureMap.values());
        if (CollectionUtils.isEmpty(itemFeatureList) ||
                MapUtils.isEmpty(contextFeatureMap) ||
                MapUtils.isEmpty(userFeatureMap) ||
                MapUtils.isEmpty(propertyMap)) {
            log.warn("/category:feature_manager/warn_name:at least one is empty in feature extractor/property_keys:{}/context_keys:{}/user_keys:{}/item_size:{}/size_source:feature_manager/device_id:{}/user_id:{}/request_id:{}",
                    propertyMap.keySet(),contextFeatureMap.keySet(), userFeatureMap.keySet(), CollectionUtils.size(itemFeatureList),request.getDeviceId(), request.getUserId(), request.getRequestId());
            return Collections.emptyMap();
        }
        Map<String, Object> rawFeatureMap = Maps.newConcurrentMap();

        propertyMap.values()
                .parallelStream()
                .forEach(e -> {
                    rawFeatureMap.put(e.getInputName(), buildEachFeature(e ,contextFeatureMap, userFeatureMap, itemFeatureList, CollectionUtils.size(itemFeatureList)));
                });

        Map<String, TensorProto> predictFeatureMap = Maps.newConcurrentMap();
        rawFeatureMap.entrySet().
                parallelStream().
                forEach(e->predictFeatureMap.put(e.getKey(), toTensorProto(propertyMap.get(e.getKey()), e.getValue())));

        log.info("/category:feature_manager/property_keys:{}/context_keys:{}/user_keys:{}/item_size:{}/size_source:feature_manager/device_id:{}/user_id:{}/request_id:{}",
                propertyMap.keySet(), contextFeatureMap.keySet(), userFeatureMap.keySet(), CollectionUtils.size(itemFeatureList),request.getDeviceId(), request.getUserId(), request.getRequestId());

        return predictFeatureMap;
    }

    private Object buildEachFeature(FeatureProperty property, Map<String, Object> contextMap, Map<String, Object> userFeatures, Collection<Record> itemProfiles, int size)  {
        Object evaluate = new Object();
        if (StringUtils.equalsIgnoreCase(property.getGroup(), NAME_CONTEXT)) {
            evaluate = getObject(contextMap, property);
        }
        if (StringUtils.equalsIgnoreCase(property.getGroup(), NAME_USER_PROFILE)) {
            evaluate = getObject(userFeatures, property);
        }
        if (StringUtils.equalsIgnoreCase(property.getGroup(), NAME_ITEM_PROFILE)) {
            evaluate = itemProfiles
                    .stream()
                    .map(e->getObject(e, property))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
        Object values = evaluate instanceof List ? evaluate : Collections.nCopies(size, evaluate);

        return values;
    }

    private TensorProto toTensorProto(FeatureProperty property, Object values){
        if (StringUtils.equalsIgnoreCase(property.getInputType(), "STRING")) {
            return makeStringFeature((List<String>) values);
        } else if (StringUtils.equalsIgnoreCase(property.getInputType(), "INT64")) {
            return makeInt64Feature((List<Long>) values);
        } else if (StringUtils.equalsIgnoreCase(property.getInputType(), "DOUBLE")) {
            return makeDoubleFeature((List<Double>) values);
        }
        throw new IllegalArgumentException("Unrecognized feature type for " + property.getInputType());
    }


    private Object getObject(Record record, FeatureProperty property) {
        Object val = record.getObject(property.getInputName()) == null ? property.getDefaultVal() : record.getObject(property.getInputName());
        if (StringUtils.equalsIgnoreCase(property.getInputType(), "STRING") && !(val instanceof String)) {
            return String.valueOf(val);
        }
        if (StringUtils.equalsIgnoreCase(property.getInputType(), "DOUBLE") && !(val instanceof Double)) {
            return Double.valueOf(String.valueOf(val));
        }
        if (StringUtils.equalsIgnoreCase(property.getInputType(), "INT64") && !(val instanceof Long)) {
            return Long.valueOf(String.valueOf(val));
        }
        return val;
    }

    private Object getObject(Map<String, Object> map, FeatureProperty property) {
        Object val = map.get(property.getInputName()) == null ? property.getDefaultVal() : map.get(property.getInputName());
        if (StringUtils.equalsIgnoreCase(property.getInputType(), "STRING") && !(val instanceof String)) {
            return String.valueOf(val);
        }
        if (StringUtils.equalsIgnoreCase(property.getInputType(), "DOUBLE") && !(val instanceof Double)) {
            return Double.valueOf(String.valueOf(val));
        }
        if (StringUtils.equalsIgnoreCase(property.getInputType(), "INT64") && !(val instanceof Long)) {
            return Long.valueOf(String.valueOf(val));
        }
        return val;
    }

    private TensorProto makeInt64Feature(List<Long> values) {
        TensorProto.Builder tpBuilder = TensorProto.newBuilder()
                .setTensorShape(TensorShapeProto.newBuilder()
                        .addDim(TensorShapeProto.Dim.newBuilder().setSize(values.size()))
                        .build()
                )
                .setDtype(DataType.DT_INT64);
        values.stream().forEach(tpBuilder::addInt64Val);
        return tpBuilder.build();
    }

    private TensorProto makeStringFeature(List<String> values) {
        TensorProto.Builder tpBuilder = TensorProto.newBuilder()
                .setTensorShape(TensorShapeProto.newBuilder()
                        .addDim(TensorShapeProto.Dim.newBuilder().setSize(values.size()))
                        .build()
                )
                .setDtype(DataType.DT_STRING);
        values.stream().map(ByteString::copyFromUtf8).forEach(tpBuilder::addStringVal);
        return tpBuilder.build();
    }
    private TensorProto makeDoubleFeature(List<Double> values) {
        TensorProto.Builder tpBuilder = TensorProto.newBuilder()
                .setTensorShape(TensorShapeProto.newBuilder()
                        .addDim(TensorShapeProto.Dim.newBuilder().setSize(values.size()))
                        .build()
                )
                .setDtype(DataType.DT_DOUBLE);
        values.stream().forEach(tpBuilder::addDoubleVal);
        return tpBuilder.build();
    }
}

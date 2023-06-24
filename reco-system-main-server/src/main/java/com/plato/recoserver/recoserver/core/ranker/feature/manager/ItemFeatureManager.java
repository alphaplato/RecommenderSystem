package com.plato.recoserver.recoserver.core.ranker.feature.manager;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.Get;
import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.client.exception.HoloClientException;
import com.alibaba.hologres.client.model.Record;
import com.alibaba.hologres.client.model.TableSchema;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureProperty;
import com.plato.recoserver.recoserver.common.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 物品特征管理，缓存2000个物品的特征
 * @author Kevin
 * @date 2022-04-08
 */
@Component
@Slf4j
public class ItemFeatureManager {

    private final static String TABLE_NAME = "tableName";
    private final static String PRIMARY_KEY = "primaryKey";

    @ApolloJsonValue("${reco.server.profile.item_conf:{}}")
    private JSONObject profileConf;

    @Autowired
    private HoloClient client;

    public String name() {
        return "item_profile_builder";
    }

    public Map<Item, Record> getFeature(List<Item> items, List<FeatureProperty> featureProperties) {
        HashSet<String> featureLists = featureProperties.stream().map(FeatureProperty::getInputName).collect(Collectors.toCollection(HashSet::new));
        return build(items, featureLists);
    }

    /**
     * read item feature with default timeout 50ms
     * @param items the feature need to get
     * @param featureLists the value of kv search
     **/
    public Map<Item, Record> build(List<Item> items, Set<String> featureLists) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyMap();
        }
        Map<Item, Record> futureMap = Maps.newHashMap();
        Map<String,Item> itemMap = Maps.newHashMap();
        List<Record> records = Lists.newArrayList();
        Stopwatch sw = Stopwatch.createStarted();
        try {
            TableSchema schema = client.getTableSchema(profileConf.getString(TABLE_NAME));
            List<Get> gets = new ArrayList<>();
            featureLists.add(profileConf.getString(PRIMARY_KEY));
            String[] selectedColumns = featureLists.toArray(new String[0]);
            String primaryKey = profileConf.getString(PRIMARY_KEY);

            items.forEach(e -> {
                String key = e.type() + "_" + e.getId();
                Get get = Get.newBuilder(schema)
                        .setPrimaryKey(primaryKey, key)
                        .withSelectedColumns(selectedColumns)
                        .build();
                gets.add(get);
                itemMap.put(key,e);
            });
            client.get(gets).forEach(e->{
                try {
                    records.add(e.get());
                } catch (InterruptedException | ExecutionException ex) {
                    log.warn("/category:item_feature_manager/warn_name:item_feature_manager-completableFuture call failed in profile service/item {}", e);
                }
            });
        } catch (HoloClientException e) {
            log.warn("/category:item_feature_manager/warn_name:schema get failed in profile service/table:{}", TABLE_NAME);
        }
        records.stream().filter(Objects::nonNull).forEach(e -> {
            futureMap.put(itemMap.get(String.valueOf(e.getObject(profileConf.getString(PRIMARY_KEY)))),e);
        });
        log.info("/category:item_feature_manager/getting item profiles from profile service input size {} output size {}[{}] cost time {} ms",items.size(), records.size(), futureMap.size() , sw.elapsed(TimeUnit.MILLISECONDS));
        return futureMap;
    }
}
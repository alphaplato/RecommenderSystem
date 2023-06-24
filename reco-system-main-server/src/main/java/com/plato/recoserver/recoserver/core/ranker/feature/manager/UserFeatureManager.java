package com.plato.recoserver.recoserver.core.ranker.feature.manager;

import com.alibaba.hologres.client.model.Record;
import com.google.common.collect.Maps;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureProperty;
import com.plato.recoserver.recoserver.core.ranker.feature.inf.IFeatureManager;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lishuguang
 * @date 2022/8/24
 **/
@Component
@Slf4j
public class UserFeatureManager implements IFeatureManager<Map<String, Object>> {

    @Override
    public Map<String, Object> getFeature(RecommendContext context, List<FeatureProperty> featureProperties) {
        Map<String, Object> featuresMap = Maps.newHashMap();
        if (context.getUserProfile() == null && CollectionUtils.isEmpty(context.getRtUserBehaviors())) {
            return featuresMap;
        }
        try {
            profileProcess(featuresMap, featureProperties ,context.getUserProfile(), context.getRtUserBehaviors());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return featuresMap;
    }


    public List<String> getColumns(Record record){
        if (record == null) {
            return Collections.emptyList();
        }
        List<String> columns = record.getBitSet().stream()
                .mapToObj(e->record.getSchema().getColumn(e).getName())
                .collect(Collectors.toCollection(LinkedList::new));
        if (CollectionUtils.isEmpty(columns)) return Collections.emptyList();
        return columns;
    }

    public void profileProcess(Map<String, Object> featuresMap, List<FeatureProperty> featureProperties, Record userProfile, List<Record> rtUserProfile) {
        featureProperties.parallelStream().forEach(e -> {
            int maxLength = e.getMultiLength() == null ? 3 : e.getMultiLength();
            String separator = e.getSeparator() != null ? e.getSeparator() : "|";
            if (e.getSourceName() == null) {
                if(userProfile != null){
                    try {
                        featuresMap.put(e.getInputName(), userProfile.getObject(e.getInputName()));
                    } catch (Exception ex){
                        log.warn("/category:feature_manager/warn_name:can't get the feature named by {} from user profile", e.getInputName());
                    }
                }
            } else {
                if (CollectionUtils.isNotEmpty(rtUserProfile)) {
                    List<Object> fs = rtUserProfile.stream()
                            .map(f -> {
                                try {
                                    return f.getObject(e.getSourceName());
                                } catch (Exception ex){
                                    return null;
                                }
                            }).filter(Objects::nonNull).limit(maxLength)
                            .collect(Collectors.toCollection(LinkedList::new));
                    if (CollectionUtils.isEmpty(fs)) {
                        log.warn("/category:feature_manager/warn_name:can't get the feature named by {} from real time profile", e.getInputName());
                    } else {
                        featuresMap.put(e.getInputName(), StringUtils.join(fs, separator));
                    }
                }
            }
        });
    }
}

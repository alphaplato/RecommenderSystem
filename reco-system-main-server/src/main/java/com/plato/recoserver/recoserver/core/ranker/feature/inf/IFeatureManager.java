package com.plato.recoserver.recoserver.core.ranker.feature.inf;

import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.ranker.feature.FeatureProperty;

import java.util.List;

/**
 * @author lishuguang
 * @date 2022/8/24
 **/
public interface IFeatureManager <T>{
    T getFeature(RecommendContext context, List<FeatureProperty> featureProperties);
}

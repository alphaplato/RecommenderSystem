package com.plato.recoserver.recoserver.core.ranker.feature.manager;

import com.plato.recoserver.recoserver.core.ranker.feature.FeatureProperty;
import com.plato.recoserver.recoserver.core.ranker.feature.inf.IFeatureManager;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.grpc.service.RecoRequest;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lishuguang
 * @date 2022/8/29
 **/
@Component
public class ContextFeatureManager implements IFeatureManager<Map<String, Object>> {

    @Override
    public Map<String, Object> getFeature(RecommendContext context, List<FeatureProperty> featureProperties) {
        return getContextMap(context.getRequest());
    }

    public Map<String, Object> getContextMap(RecoRequest request){
        Map<String, Object> contextMap = new ConcurrentHashMap<>();
        Field[] fields = request.getClass().getDeclaredFields();
        Arrays.stream(fields).parallel().forEach(e->{
            e.setAccessible(true);
            try {
                contextMap.put(e.getName(), e.get(request));
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        });
        return contextMap;
    }
}

package com.plato.recoserver.recoserver.core.context.builder;

import com.google.common.base.Stopwatch;
import com.plato.recoserver.recoserver.core.abtest.ABTestManager;
import com.plato.recoserver.recoserver.core.abtest.ABTestProperty;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.context.builder.inf.IRecommendCxtBuilder;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @date 2022-04-14
 */
@Component
@Slf4j
public class RecoABPlanBuilder implements IRecommendCxtBuilder {
    @Autowired
    private ABTestManager abTestManager;

    @Override
    public String name() {
        return "ab_builder";
    }

    @Override
    public void build(RecommendContext recommendContext) {
        Stopwatch sw = Stopwatch.createStarted();
        RecoRequest request = recommendContext.getRequest();
        ABTestProperty abTestProperty = abTestManager.getABProperties(recommendContext);;
        recommendContext.setAbTestProperty(abTestProperty);
        if (MapUtils.isEmpty(abTestProperty.getPropertymap())) {
            log.warn("/category:ab_test/warn_name:ab_test-no ab test configure is found, use default plan/device_id:{}/user_id:{}/request_id:{}",
                    request.getDeviceId(),request.getUserId(), request.getRequestId());
        }else {
            List<String> groups_name = abTestProperty.getPropertymap()
                    .entrySet()
                    .stream()
                    .map(e->e.getKey() + ":" + e.getValue().getGroup())
                    .collect(Collectors.toCollection(LinkedList::new));
            log.info("/category:ab_test/group_names_hit:{}/device_id:{}/user_id:{}/request_id:{}",
                    groups_name, request.getDeviceId(), request.getUserId(), request.getRequestId());
        }
        log.info("/category:context/device_id:{}/user_id:{}/request_id:{}/cost_time:{}/cost_time_name:{}",
                request.getDeviceId(), request.getUserId(), request.getRequestId(), sw.elapsed(TimeUnit.MILLISECONDS), name());
    }
}

package com.plato.recoserver.recoserver.core.context.builder;

import com.google.common.base.Stopwatch;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.builder.inf.IRecommendCxtBuilder;
import com.plato.recoserver.recoserver.core.service.SessionStorageService;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @date 2022-04-07
 */
@Component
@Slf4j
public class RecommendHistoryBuilder implements IRecommendCxtBuilder {

    @Autowired
    private SessionStorageService sessionStorageService;

    @Override
    public String name() {
        return "history_builder";
    }

    @Override
    public void build(RecommendContext recommendContext) {
        Stopwatch sw = Stopwatch.createStarted();
        RecoRequest request = recommendContext.getRequest();
        Set<Item> histories = sessionStorageService.getRecHistory(recommendContext);
        recommendContext.setRecHistory(histories);
        log.info("/category:context/device_id:{}/user_id:{}/request_id:{}/cost_time:{}/result_size:{}/cost_time_name:{}",
                request.getDeviceId(), request.getUserId(), request.getRequestId(), sw.elapsed(TimeUnit.MILLISECONDS), CollectionUtils.size(histories), name());
    }
}

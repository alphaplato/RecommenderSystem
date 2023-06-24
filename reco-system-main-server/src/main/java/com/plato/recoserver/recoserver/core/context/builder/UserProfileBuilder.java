package com.plato.recoserver.recoserver.core.context.builder;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.Get;
import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.client.exception.HoloClientException;
import com.alibaba.hologres.client.model.TableSchema;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.google.common.base.Stopwatch;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.context.builder.inf.IRecommendCxtBuilder;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author lishuguang
 * @date 2022-06-07
 */
@Component
@Slf4j
public class UserProfileBuilder implements IRecommendCxtBuilder {

    private final static String TABLE_NAME = "tableName";
    private final static String PRIMARY_KEY = "primaryKey";

    @ApolloJsonValue("${reco.server.profile.user_conf}")
    private JSONObject profileConf;

    @Autowired
    private HoloClient client;

    @Override
    public String name() {
        return "user_profile_builder";
    }

    @Override
    public void build(RecommendContext recommendContext) {
        Stopwatch sw = Stopwatch.createStarted();
        RecoRequest request = recommendContext.getRequest();
        String key = request.getDeviceId();
        if (request.getUserId() > 0) {
            key = String.valueOf(request.getUserId());
        }
        try {
            TableSchema schema = client.getTableSchema(profileConf.getString(TABLE_NAME));
            Get get = Get.newBuilder(schema).setPrimaryKey(profileConf.getString(PRIMARY_KEY), key).build();
            recommendContext.setUserProfile(client.get(get).get());
        } catch (HoloClientException | InterruptedException | ExecutionException e) {
            log.warn("/category:context/warn_name:timeout of getting user profiles/device_id:{}/user_id:{}/request_id:{}",
                    request.getDeviceId(), request.getUserId(), request.getRequestId());
        }
        log.info("/category:context/data from {}/device_id:{}/user_id:{}/request_id:{}/cost_time:{}/cost_time_name:{}",
                profileConf.getString(TABLE_NAME), request.getDeviceId(), request.getUserId(), request.getRequestId(),
                sw.elapsed(TimeUnit.MILLISECONDS), name());
    }
}

package com.plato.recoserver.recoserver.core.context.builder;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.client.Scan;
import com.alibaba.hologres.client.SortKeys;
import com.alibaba.hologres.client.exception.HoloClientException;
import com.alibaba.hologres.client.model.Record;
import com.alibaba.hologres.client.model.RecordScanner;
import com.alibaba.hologres.client.model.TableSchema;
import com.google.common.base.Stopwatch;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.context.builder.inf.IRecommendCxtBuilder;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Kevin
 * @date 2022-04-21
 */
@Component
@Slf4j
public class RealTimeProfileBuilder implements IRecommendCxtBuilder {
    private final static String TABLE_NAME = "tableName";

    @Autowired
    private HoloClient holoClient;

    @Override
    public String name() {
        return "rt_profile_builder";
    }

    @Override
    public void build(RecommendContext recommendContext) {
        Stopwatch sw = Stopwatch.createStarted();
        RecoRequest request = recommendContext.getRequest();
        String key = request.getDeviceId();
        if (request.getUserId() > 0) {
            key = String.valueOf(request.getUserId());
        }
        JSONObject profileConf = recommendContext.getRecommendConfiguration().getRtProfileConf();

        List<Record> records = new LinkedList<>();
        Calendar calendar = Calendar.getInstance();
        int duration = profileConf.getIntValue("time");
        long endTime = calendar.getTimeInMillis() + 10;
        calendar.add(Calendar.DATE, -1 * duration);
        long startTime = calendar.getTimeInMillis();

        String[] selectedColumns = JSONObject
                .parseArray(profileConf.getJSONArray("selectedColumns").toJSONString(), String.class)
                .toArray(new  String[0]);
        try {
            TableSchema schema = holoClient.getTableSchema(profileConf.getString(TABLE_NAME));
            Scan scan = Scan.newBuilder(schema)
                    .withSelectedColumns(selectedColumns)
                    .addEqualFilter("uid", key)
                    .addRangeFilter("time",startTime, endTime)
                    .setSortKeys(SortKeys.CLUSTERING_KEY)
                    .build();
            RecordScanner rs = holoClient.scan(scan);
            while (rs.next()){
                records.add(rs.getRecord());
            }
        }  catch (HoloClientException e) {
            log.warn("/category:context/warn_name:timeout of getting real time user profiles/device_id:{}/user_id:{}/request_id:{}",
                request.getDeviceId(), request.getUserId(), request.getRequestId());
        }
        if (CollectionUtils.isEmpty(records)) {
            log.info("/category:context/warn_name:context-no real time profile as no user id/device_id:{}/user_id:{}/request_id:{}",
                    request.getDeviceId(), request.getUserId(), request.getRequestId());
        }
        Collections.reverse(records);
        recommendContext.setRtUserBehaviors(records);
        log.info("/category:context/data from {}/device_id:{}/user_id:{}/request_id:{}/cost_time:{}/result_size:{}/cost_time_name:{}",
                profileConf.getString(TABLE_NAME), request.getDeviceId(), request.getUserId(), request.getRequestId(),
                sw.elapsed(TimeUnit.MILLISECONDS),CollectionUtils.size(records), name());
    }
}

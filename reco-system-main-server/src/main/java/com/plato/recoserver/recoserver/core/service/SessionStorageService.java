package com.plato.recoserver.recoserver.core.service;

import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.datamanager.dao.redis.JedisClient;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.grpc.service.RecoRequest;
import com.plato.recoserver.recoserver.util.RecConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Response;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @date 2022-03-25
 */
@Service
@Slf4j
public class SessionStorageService {

    @Autowired
    private JedisClient historyJedis;

    //历史记录为1天有效期
    private long HIS_EXPIRE_DURATION = TimeUnit.DAYS.toMillis(1L);

    @Async
    public Future<Boolean> saveRecHistory(RecommendContext recommendContext, List<Item> items) {
        RecoRequest request = recommendContext.getRequest();
        String sid = request.getDeviceId();
        if (request.getUserId() > 0) {
            sid = String.valueOf(request.getUserId());
        }
        if (CollectionUtils.isEmpty(items)) {
            log.warn("/category:session_storage/warn_name:session_storage-no items recommended/device_id:{}/user_id:{}/request_id:{}",
                    request.getDeviceId(), recommendContext.getRequest().getUserId(), recommendContext.getRequest().getRequestId());
            return new AsyncResult<>(Boolean.FALSE);
        }
        final String key = RecConstants.RECO_HISTORY_PREFIX + sid;
        final Double currTime = (double) System.currentTimeMillis();
        Map<String, Double> itemsMap = new HashMap<>();
        items.stream()
                .map(e -> e.type() + "_" + Objects.toString(e.getId()))
                .forEach(e -> itemsMap.put(e, currTime));
        historyJedis.execute(jedis -> jedis.zadd(key, itemsMap));
        historyJedis.execute(jedis -> jedis.pexpire(key, HIS_EXPIRE_DURATION));
        log.info("/category:session_storage/size:{}/size_source:session_write/device_id:{}/user_id:{}/request_id:{}",
                items.size(), request.getDeviceId(), request.getUserId(), request.getRequestId());
        return new AsyncResult<>(Boolean.TRUE);
    }

    public Set<Item> getRecHistory(RecommendContext recommendContext) {
        RecoRequest request = recommendContext.getRequest();
        if(!(request.getUserId() > 0 || StringUtils.length(request.getDeviceId()) > 0)) {
            log.warn("/category:session_storage/warn_name: user and device can be unable to identify");
            return Collections.emptySet();
        }
        HashSet<String> keys = new HashSet<>();
        if (request.getUserId() > 0)  keys.add(RecConstants.RECO_HISTORY_PREFIX + request.getUserId());
        if ( StringUtils.length(request.getDeviceId()) > 0) keys.add(RecConstants.RECO_HISTORY_PREFIX + request.getDeviceId());
        final double min = (double)(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1L));
        final double now = (double) System.currentTimeMillis();
        //获取最近一天内的推荐记录
        List<Response<Set<String>>> responses = historyJedis.executeRPipeline(keys, (jedis, key) -> jedis.zrangeByScore(key, min, now));
        Set<String> his = new HashSet<>();
        responses.stream().map(Response::get).filter(Objects::nonNull).forEach(his::addAll);

        if (CollectionUtils.isEmpty(his)) {
            return Collections.emptySet();
        }
        Set<Item> items = his.stream().map(e -> e.split("_"))
                .filter(e -> e.length > 1)
                .map(e -> new CandidateItem(NumberUtils.toLong(e[1]), NumberUtils.toInt(e[0]), 0.0d))
                .filter(e -> e.getId() > 0 && e.type() > 0)
                .collect(Collectors.toSet());
        log.info("/category:session_storage/size:{}/size_source:session_read/device_id:{}/user_id:{}/request_id:{}",
                items.size(), request.getDeviceId(), request.getUserId(), request.getRequestId());
        return items;
    }
}

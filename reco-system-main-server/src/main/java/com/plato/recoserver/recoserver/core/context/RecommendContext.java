package com.plato.recoserver.recoserver.core.context;

import com.alibaba.hologres.client.model.Record;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.plato.recoserver.grpc.service.RecoRequest;
import com.plato.recoserver.recoserver.SpringUtil;
import com.plato.recoserver.recoserver.core.abtest.ABTestConfiguration;
import com.plato.recoserver.recoserver.core.abtest.ABTestProperty;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.builder.inf.IRecommendCxtBuilder;
import com.plato.recoserver.recoserver.core.retrieval.filter.ReadFilter;
import com.plato.recoserver.recoserver.core.retrieval.filter.RecHistoryFilter;
import com.plato.recoserver.recoserver.core.retrieval.filter.SpecifiedFilter;
import com.plato.recoserver.recoserver.util.IdGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Kevin
 * @date 2022-03-07
 */
@Slf4j
public class RecommendContext {

    private final static ThreadPoolTaskExecutor cxtBuilderExecutor;

    static {
        cxtBuilderExecutor = SpringUtil.getBean("cxtBuilderExecutor", ThreadPoolTaskExecutor.class);
    }

    @Getter
    @Setter
    private Record userProfile;

    @Getter
    @Setter
    public Map<Item, Record> itemProfiles;
    @Getter
    private final RecoRequest request;
    /**
     * ab test分流信息
     */
    @Setter
    @Getter
    private ABTestProperty abTestProperty = new ABTestProperty();

    @Getter
    private final String eventId;

    @Getter
    private Set<Item> specifiedFilter = Collections.emptySet();
    @Getter
    @Setter
    private Set<Item> recHistory = Collections.emptySet();

    private Set<Item> recommended =  new HashSet<>();

    @Getter
    @Setter
    private List<Record> rtUserBehaviors;

    @Setter
    @Getter List<Record> rtUserQueries;

    @Getter
    @Setter
    private Map<String, List<Item>> retrievalMap;
    @Getter
    @Setter
    private ReadFilter readFilter;

    @Getter
    @Setter
    private RecHistoryFilter recHistoryFilter;

    @Getter
    @Setter
    private SpecifiedFilter specifiedFilterF;


    @Getter
    @Setter
    private RecommendConfiguration recommendConfiguration;

    @Getter
    @Setter
    private ABTestConfiguration abTestConfiguration;

    private Map<IRecommendCxtBuilder, Future<?>> futureMap = Maps.newHashMap();

    public RecommendContext(RecoRequest request) {
        Objects.requireNonNull(request);
        this.request = request;
        this.eventId = IdGenerator.generateEventId();
    }

    public void setSpecifiedFilter(Set<Item> filters) {
        this.specifiedFilter = Collections.unmodifiableSet(filters);
    }

    public boolean addToRecommended(Item item) {
        return recommended.add(item);
    }

    public void addBuilder(IRecommendCxtBuilder builder) {
        Future<?> future = cxtBuilderExecutor.submit(() -> builder.build(this));
        futureMap.put(builder, future);
    }

    public void waitBuilderDone(long timeout, TimeUnit unit) {
        Stopwatch sw = Stopwatch.createStarted();
        if (timeout < 0) {
            throw new IllegalArgumentException("/category:context/Invalid timeout specified for waiting builder done");
        }
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        Iterator<Map.Entry<IRecommendCxtBuilder, Future<?>>> iterator = futureMap.entrySet().iterator();
        while (iterator.hasNext() && System.nanoTime() < deadline) {
            Map.Entry<IRecommendCxtBuilder, Future<?>> entry = iterator.next();
            if (!entry.getValue().isDone()) {
                try {
                    entry.getValue().get(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
                } catch (TimeoutException e) {
                    log.warn("/category:context/warn_name:Timeout exception waiting for builder {} done/device_id:{}/user_id:{}/request_id:{}",
                            entry.getKey().name(), request.getDeviceId(), request.getUserId(), request.getRequestId());
                } catch (Exception e) {
                    log.warn("/category:context/warn_name:Exception occurred waiting for builder {} done/device_id:{}/user_id:{}/request_id:{}",
                            entry.getKey().name(), request.getDeviceId(), request.getUserId(), request.getRequestId());
                }
            }
            iterator.remove();
        }
        //check if the remaining builders are done
        while (iterator.hasNext()) {
            Map.Entry<IRecommendCxtBuilder, Future<?>> entry = iterator.next();
            if (entry.getValue().isDone()) {
                iterator.remove();
            }
        }
        log.info("/category:context/device_id:{}/user_id:{}/request_id:{}/cost_time_name:wait_for_all_builder_done/cost_time:{}/size:{}/size_source:remaining_builders_count",
                request.getDeviceId(), request.getUserId(), request.getRequestId(), sw.elapsed(TimeUnit.MILLISECONDS), futureMap.size());
        futureMap = null;//help gc
    }
}

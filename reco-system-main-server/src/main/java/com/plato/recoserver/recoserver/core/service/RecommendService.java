package com.plato.recoserver.recoserver.core.service;

import com.google.common.base.Stopwatch;
import com.plato.recoserver.recoserver.core.abtest.ABTestConfiguration;
import com.plato.recoserver.recoserver.core.context.RecommendConfiguration;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.context.builder.*;
import com.plato.recoserver.recoserver.core.ranker.inf.IRanker;
import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IReranker;
//import com.plato.platform.spread.regulate.grpc.lib.RegulateResult;
//import com.plato.platform.spread.regulate.grpc.lib.SpreadRegulatorServiceGrpc;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.retrieval.RecallParallel;
import com.plato.recoserver.recoserver.core.retrieval.filter.ReadFilter;
import com.plato.recoserver.recoserver.core.retrieval.filter.RecHistoryFilter;
import com.plato.recoserver.recoserver.core.retrieval.filter.RegulatorFilter;
import com.plato.recoserver.recoserver.core.retrieval.filter.SpecifiedFilter;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.Math.min;

/**
 * @author lishuguang
 * @date 2022-03-18
 */
@Slf4j
@Service
public class RecommendService {

    @Value("${reco.server.builder.timeout:50}")
    private long builderTimeout;
    @Autowired
    private ThreadPoolTaskExecutor asyncTaskExecutor;
    @Autowired
    private RecallParallel recallparallel;
    @Autowired
    private IRanker ranker;
    @Autowired
    private IReranker reranker;
    @Autowired
    private RecommendHistoryBuilder recommendHistoryBuilder;
    @Autowired
    private UserProfileBuilder userProfileBuilder;
    @Autowired
    private RecoABPlanBuilder recoABPlanBuilder;
    @Autowired
    private RealTimeProfileBuilder realTimeProfileBuilder;
    @Autowired
    private RealTimeQueryBuilder realTimeQueryBuilder;
    @Autowired
    private ReadFilter readFilter;
    @Autowired
    private RegulatorFilter regulatorFilter;
    @Autowired
    private RecHistoryFilter recHistoryFilter;
    @Autowired
    private SpecifiedFilter specifiedFilterF;
    @Autowired
    private SessionStorageService sessionStorageService;
    @Autowired
    private RecommendConfiguration recommendConfiguration;
    @Autowired
    private ABTestConfiguration abTestConfiguration;
//    @GrpcClient("spread-regulator")
//    private SpreadRegulatorServiceGrpc.SpreadRegulatorServiceBlockingStub spreadRegulatorServiceBlockingStub;
//


    public List<Item> recommend(final RecommendContext recommendContext) {
        Stopwatch sw = Stopwatch.createStarted();
        RecoRequest request = recommendContext.getRequest();
        log.info("/category:system/system_name:recommend starts/device_id:{}/user_id:{}/request_id:{}", request.getDeviceId(), request.getUserId(), request.getRequestId());
        recommendContext.setRecommendConfiguration(recommendConfiguration); // 初始化系统配置，最优先
        recommendContext.setAbTestConfiguration(abTestConfiguration); //初始化实验配置
        recoABPlanBuilder.build(recommendContext); // 考虑到 builder 也需要做实验，外置在最前面
        recommendContext.addBuilder(realTimeQueryBuilder);
        recommendContext.addBuilder(recommendHistoryBuilder);
        recommendContext.addBuilder(userProfileBuilder);
        recommendContext.addBuilder(realTimeProfileBuilder);
        recommendContext.setReadFilter(readFilter);
        recommendContext.setRecHistoryFilter(recHistoryFilter);
        recommendContext.setSpecifiedFilterF(specifiedFilterF);
        //wait for all builders done, maximum time out is 50 milliseconds
        recommendContext.waitBuilderDone(builderTimeout, TimeUnit.MILLISECONDS);


        // 特殊过滤1 开学季 临时需求 存放到 session storage
        List<Item> sessionList = request
                .getFiltersList()
                .stream().filter(Objects::nonNull).limit(1000)
                .map(e -> new CandidateItem(e.getItemId(), e.getType().getNumber(),0.0))
                .collect(Collectors.toCollection(LinkedList::new));
        // 特殊过滤2 海洛图册 非 pc端 不适配，请求携带
        Set<Item> helloAlbumsFilters = recommendContext.getRequest()
                .getFiltersList()
                .stream().filter(e-> (e != null && e.getItemId() == 0))
                .map(e -> new CandidateItem(e.getItemId(), e.getType().getNumber(),0.0))
                .collect(Collectors.toCollection(HashSet::new));
        recommendContext.setSpecifiedFilter(helloAlbumsFilters);
        log.info("/category:retrieval/helloAlbumsFilters:{}/helloAlbumsFilters_size:{}",
                helloAlbumsFilters, CollectionUtils.size(helloAlbumsFilters));

        List<Item> candidates = recallparallel.recallParallel(recommendContext);
        log.info("/category:retrieval/items retrieved for user {} with id {}, request {} are {}/size:{}/size_source:retrievalList",
                request.getDeviceId(), request.getUserId(), request.getRequestId(), candidates, candidates.size());
        //rank items
        List<Item> rankedList = taskParallel(recommendContext, candidates);
        log.info("/category:rank/items after rank for user {} with id {}, request {} are {}/size:{}/size_source:ranked_list/request_get_num:{}",
                request.getDeviceId(), request.getUserId(), request.getRequestId(), rankedList, rankedList.size(), request.getNum());
        //rerank item
        List<Item> rerankList = reranker.rerank(recommendContext, rankedList);

        // 排重，待优化
        List<Item> distinctItems = new LinkedList<>();
        rerankList.stream().forEach(e->{if(!distinctItems.contains(e)) distinctItems.add(e);});

        List<Item> finalList = distinctItems.stream()
                .limit(recommendContext.getRequest().getNum())
                .collect(Collectors.toCollection(LinkedList::new));

        //new final_list set, count distinct pids
        Set<String> finalSet = finalList.stream().map(e -> e.type() + "_" + e.getId()).collect(Collectors.toCollection(HashSet::new));
        log.info("/category:rerank/items after rerank for user {} with id {}, request {} are {}", request.getDeviceId(), request.getUserId(), request.getRequestId(), finalList);
        sessionList.addAll(finalList);
        sessionStorageService.saveRecHistory(recommendContext, sessionList);
        int requestNum = request.getNum();
        log.info("/category:system/strategy_distribution:{}/source_distribution:{}/final_list_size:{}/final_set_size:{}/request_get_num:{}/final_list_request_get_num_diff:{}/request_get_num_final_set_diff:{}/device_id:{}/user_id:{}/request_id:{}/cost_time_name:recommend_finished/cost_time:{}",
                finalList.stream().collect(Collectors.groupingBy(e->((CandidateItem) e).getStrategy(),Collectors.counting())),
                finalList.stream().collect(Collectors.groupingBy(e->((CandidateItem) e).getSource(),Collectors.counting())),
                finalList.size(), finalSet.size(), requestNum, finalList.size() - requestNum, requestNum - finalSet.size(), request.getDeviceId(), request.getUserId(), request.getRequestId(), sw.elapsed(TimeUnit.MILLISECONDS));
        //save recommend history
        return finalList;
    }
    private List<Item> taskParallel(RecommendContext context, List<Item> candidates) {
        RecoRequest request = context.getRequest();
        Future<List<Item>> rankFuture = asyncTaskExecutor.submit(()->ranker.rank(context, candidates));
//        Future<List<RegulateResult>> regulatorFuture = asyncTaskExecutor.submit(() -> getRegulatorList(context, candidates));
//        List<RegulateResult> regulatedList;
        List<Item> rankList;
        try {
            rankList = rankFuture.get();
        } catch (Exception e){
            rankList = candidates;
        }
//        try {
//            regulatedList = regulatorFuture.get();
//        } catch (Exception e){
//            regulatedList = Collections.emptyList();
//        }
//        Set<Item> finalRegulatedList = regulatedList.stream()
//                .map(e-> {
//                    return new CandidateItem(e.getItem().getId(),e.getItem().getType().getNumber(),0);
//                })
//                .collect(Collectors.toCollection(HashSet::new));

        List<Item> logCandidateItems = new LinkedList<>();
        List<Item> filteredList = new LinkedList<>();
        candidates.forEach(e -> {
//            if(finalRegulatedList.contains(e)){
//                CandidateItem c = (CandidateItem) e;
//                c.setFilterType(CandidateItem.FilterType.REGULATED);
//                logCandidateItems.add(c);
//            }else {
//                filteredList.add(e);
//            }
            filteredList.add(e);
        });

        if (CollectionUtils.size(filteredList) < request.getNum()) {
            log.warn("/category:regulator/items are after regulator filtered /filtered_size:{}/device_id:{}/user_id:{}/request_id:{}/filtered_items:{}",
                    filteredList.size(), request.getDeviceId(), request.getUserId(), request.getRequestId(), logCandidateItems);
        }else {
            log.info("/category:regulator/items are after regulator filtered /filtered_size:{}/device_id:{}/user_id:{}/request_id:{}",
                    filteredList.size(), request.getDeviceId(), request.getUserId(), request.getRequestId());
        }
        return filteredList;
    }

//    private List<RegulateResult> getRegulatorList(RecommendContext context, List<Item> candidates){
//        Stopwatch sw = Stopwatch.createStarted();
//        RecoRequest request = context.getRequest();
//        List<RegulateResult> regulateResponse = regulatorFilter.getRegulatorList(context, candidates);
//        List<String> regulateResponseLog = regulateResponse.stream().map(e -> (e.getItem().getType() + "_" + e.getItem().getId() + "=" + e.getMsg())).collect(Collectors.toCollection(LinkedList::new));
//        log.info("items are regulated by regulator/items:{}/item_size:{}/device_id:{}/user_id:{}/request_id:{}/cost_time_name:regulator_filter/cost_time:{}",
//                regulateResponseLog, CollectionUtils.size(regulateResponse), request.getDeviceId(), request.getUserId(), request.getRequestId(), sw.elapsed(TimeUnit.MILLISECONDS));
//        return regulateResponse;
//    }
}

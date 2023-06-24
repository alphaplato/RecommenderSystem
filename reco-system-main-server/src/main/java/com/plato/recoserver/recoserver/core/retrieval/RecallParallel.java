package com.plato.recoserver.recoserver.core.retrieval;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plato.recoserver.recoserver.core.abtest.ABTestConfiguration;
import com.plato.recoserver.recoserver.core.abtest.ABTestProperty;
import com.plato.recoserver.recoserver.core.context.RecommendConfiguration;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.context.RecoServerAppContext;
import com.plato.recoserver.recoserver.core.retrieval.inf.IRetrieval;
import com.plato.recoserver.grpc.service.RecoRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Slf4j
@Component
public class RecallParallel {
    @Value("${reco.server.retrieval.timeout:100}")
    private long retrievalTimeout;
    private int DEFAULT_RANK_NUM = 300;
    private int default_each_retrieval_num = 10;
    private final String DEFAULT_FINAL_RETRIEVAL = "all_recall";
    private final int DEFAULT_RETRIEVAL_NUM = 40;

    @Autowired
    private RecoServerAppContext recoServerAppContext;

    @Autowired
    private ThreadPoolTaskExecutor retrievalExecutor;

    public List<Item> recallParallel(RecommendContext recommendContext) {
        return recallParallel(recommendContext, TimeUnit.MILLISECONDS.toNanos(retrievalTimeout));
    }
    private List<Item> recallParallel(RecommendContext recommendContext, long timeout) {
        RecoRequest request = recommendContext.getRequest();
        Stopwatch sw = Stopwatch.createStarted();
        List<RecommendConfiguration.ChannelConf> channels = recommendContext.getRecommendConfiguration().getChannelConfs();
        // 召回实验****************
        ABTestProperty abTestProperty = recommendContext.getAbTestProperty();
        ABTestConfiguration abTestConfiguration = recommendContext.getAbTestConfiguration();
        if(MapUtils.isNotEmpty(abTestProperty.getPropertymap()) && abTestProperty.getPropertymap().get("retrieval_new_channel_exp") != null) {
            String group = abTestProperty.getPropertymap().get("retrieval_new_channel_exp").getGroup();
            if (StringUtils.equals(group,"vec_recall_a1")){
                List<RecommendConfiguration.ChannelConf> abChannels = abTestConfiguration.getChannelConfs1();
                if(CollectionUtils.size(abChannels) > 5)  channels = abChannels;
            } else if (StringUtils.equals(group,"vec_recall_a2")) {
                List<RecommendConfiguration.ChannelConf> abChannels = abTestConfiguration.getChannelConfs2();
                if(CollectionUtils.size(abChannels) > 5)  channels = abChannels;
            } else if (StringUtils.equals(group,"vec_recall_a3")) {
                List<RecommendConfiguration.ChannelConf> abChannels = abTestConfiguration.getChannelConfs3();
                if(CollectionUtils.size(abChannels) > 5)  channels = abChannels;
            } else if (StringUtils.equals(group,"vec_recall_a4")) {
                List<RecommendConfiguration.ChannelConf> abChannels = abTestConfiguration.getChannelConfs4();
                if(CollectionUtils.size(abChannels) > 5)  channels = abChannels;
            } else if (StringUtils.equals(group,"vec_recall_a5")) {
                List<RecommendConfiguration.ChannelConf> abChannels = abTestConfiguration.getChannelConfs5();
                if(CollectionUtils.size(abChannels) > 5)  channels = abChannels;
            } else if (StringUtils.equals(group,"vec_recall_a6")) {
                List<RecommendConfiguration.ChannelConf> abChannels = abTestConfiguration.getChannelConfs6();
                if(CollectionUtils.size(abChannels) > 5)  channels = abChannels;
            } else if (StringUtils.equals(group,"vec_recall_a7")) {
                List<RecommendConfiguration.ChannelConf> abChannels = abTestConfiguration.getChannelConfs7();
                if(CollectionUtils.size(abChannels) > 5)  channels = abChannels;
            } else if (StringUtils.equals(group,"vec_recall_a8")) {
                List<RecommendConfiguration.ChannelConf> abChannels = abTestConfiguration.getChannelConfs8();
                if(CollectionUtils.size(abChannels) > 5)  channels = abChannels;
            } else if (StringUtils.equals(group,"vec_recall_a9")) {
                List<RecommendConfiguration.ChannelConf> abChannels = abTestConfiguration.getChannelConfs9();
                if(CollectionUtils.size(abChannels) > 5)  channels = abChannels;

                /*DEFAULT_RANK_NUM = abTestConfiguration.getDEFAULT_RANK_NUM_AB() > 300 ?
                        abTestConfiguration.getDEFAULT_RANK_NUM_AB() : DEFAULT_RANK_NUM;
                default_each_retrieval_num = abTestConfiguration.getDefault_each_retrieval_num() > default_each_retrieval_num ?
                        abTestConfiguration.getDefault_each_retrieval_num() : default_each_retrieval_num;
                log.info("/category:retrieval/DEFAULT_RANK_NUM:{}/default_each_retrieval_num:{}//device_id:{}/user_id:{}/request_id:{}",
                        DEFAULT_RANK_NUM, default_each_retrieval_num,request.getDeviceId(), request.getUserId(), request.getRequestId());*/
            }
        }
        // ***********************
        Map<String, Future<List<CandidateItem>>> futureMap = Maps.newHashMap();
        HashMap<String, Float> retrievalMapPriorityConf = new HashMap<String, Float>();  // 配置的map

        //retrieve items parallel
        if (CollectionUtils.isEmpty(channels)) {
            log.error("/category:retrieval/error_name: cannot find retrieval default plan for user {}",
                    request.getDeviceId());
            return Collections.emptyList();
        }
        int defaultSize = request.getNum() > 0 ? request.getNum() : DEFAULT_RETRIEVAL_NUM;
        for (RecommendConfiguration.ChannelConf channel : channels) {
            String retrievalName = channel.getName();
            IRetrieval retrieval = recoServerAppContext.getRetrieval(retrievalName);
            if (null == retrieval) {
                log.warn("/category:retrieval/warn_name:no retrieval found/device_id:{}/user_id:{}/request_id:{}/name:{}",
                        request.getDeviceId(), request.getUserId(), request.getRequestId(), retrievalName);
                continue;
            }
            int sizeInPlan = channel.getQuota();
            int size = sizeInPlan > 0 ? sizeInPlan : defaultSize;
            Future<List<CandidateItem>> future = retrievalExecutor.submit(() -> retrieval.getCandidates(recommendContext, size));
            futureMap.put(retrievalName, future);
            retrievalMapPriorityConf.put(retrievalName, channel.getPriority());
            log.info("/category:plan_retrieval/retrievalName:{}/sizeInPlan:{}/channel priority:{}/user_id:{}/device_id:{}/request_id:{}",
                    retrievalName, sizeInPlan,channel.getPriority(), request.getUserId(), request.getDeviceId(), request.getRequestId());
        }
        //wait for retrieval result
        long deadline = System.nanoTime() + timeout;
        Iterator<Map.Entry<String, Future<List<CandidateItem>>>> iterator = futureMap.entrySet().iterator();
        HashMap<String, List<CandidateItem>> retrievalMap = new HashMap<>();
        int retrievalSum = 0;
        float prioritySum  = 0f;
        while (iterator.hasNext() && System.nanoTime() < deadline) {
            Map.Entry<String, Future<List<CandidateItem>>> entry = iterator.next();
            Future<List<CandidateItem>> future = entry.getValue();
            try {
                List<CandidateItem> items = future.get(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
                retrievalMap.put(entry.getKey(), items);
                int itemNumbs = Math.toIntExact(
                        items.stream()
                                .filter(e -> e.getFilterType() == CandidateItem.FilterType.NORMAL)
                                .count());

                if (!entry.getKey().contains(DEFAULT_FINAL_RETRIEVAL)) retrievalSum += itemNumbs;
                prioritySum += retrievalMapPriorityConf.get(entry.getKey());
            } catch (TimeoutException e) {
                log.warn("/category:retrieval/warn_name:retrieve item from retrieval {} timeout/device_id:{}/user_id:{}/request_id:{}",
                        entry.getKey(), request.getDeviceId(), request.getUserId(), request.getRequestId());
            } catch (Exception e) {
                log.warn("/category:retrieval/warn_name:warn while waiting for retrieval {}/device_id:{}/user_id:{}/request_id:{}/id {}",
                        entry.getKey(), request.getDeviceId(), request.getUserId(), request.getRequestId(), e);
            }
        }
        //check if the future is done after overall timeout
        while (iterator.hasNext()) {
            Map.Entry<String, Future<List<CandidateItem>>> entry = iterator.next();
            Future<List<CandidateItem>> future = entry.getValue();
            if (future.isDone()) {
                try {
                    List<CandidateItem> items = future.get();
                    retrievalMap.put(entry.getKey(), items);
                    int itemNumbs = Math.toIntExact(
                            items.stream()
                                    .filter(e -> e.getFilterType() == CandidateItem.FilterType.NORMAL)
                                    .count());

                    if (!entry.getKey().contains(DEFAULT_FINAL_RETRIEVAL)) retrievalSum += itemNumbs;
                    prioritySum += retrievalMapPriorityConf.get(entry.getKey());
                } catch (Exception e) {
                    log.warn("/category:retrieval/warn_name:retrieval-warn while waiting for retrieval {}/device_id:{}/user_id:{}/request_id:{}",
                            entry.getKey(), request.getDeviceId(), request.getUserId(), request.getRequestId(), e);
                }
            } else {
                log.warn("/category:retrieval/warn_name:retrieval-item from retrieval {} timeout/device_id:{}/user_id:{}/request_id:{}",
                        entry.getKey(), request.getDeviceId(), request.getUserId(), request.getRequestId());
                log.info("/category:retrieval/size:null/size_source:{}/device_id:{}/user_id:{}/request_id:{}",
                        entry.getKey(), request.getDeviceId(), request.getUserId(), request.getRequestId());
            }
        }

        // 连接外部的接口, retrieval内部处理逻辑用不到
        HashMap<String, List<Item>> retrievalMapOut = (HashMap<String, List<Item>>)retrievalMap.clone();
        recommendContext.setRetrievalMap(retrievalMapOut);
        log.info("/category:retrieval/retrieval names:{}/retrieval out name:{}/device_id:{}/user_id:{}/request_id:{}",
                retrievalMap.keySet(), retrievalMapOut.keySet(), request.getDeviceId(), request.getUserId(), request.getRequestId());

        // List<Item> candidates = quotaReserve(recommendContext, retrievalMap, retrievalSum);
        List<Item> candidates = quotaReserve(recommendContext, retrievalMap, retrievalSum, prioritySum, retrievalMapPriorityConf);
        log.info("/category:retrieval/size:{}/size_source:all_retrievals/device_id:{}/user_id:{}/request_id:{}/cost_time_name:all_retrievals/cost_time:{}",
                candidates.size(), request.getDeviceId(), request.getUserId(), request.getRequestId(), sw.elapsed(TimeUnit.MILLISECONDS));
        //Collections.shuffle(candidates);
        return candidates.stream()
                .limit(DEFAULT_RANK_NUM)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private List<Item> quotaReserve(RecommendContext recommendContext, HashMap<String, List<CandidateItem>> retrievalList,
                                    int retrievalSum, float prioritySum, HashMap<String, Float> retrievalMapPriorityConf) {
        RecoRequest request = recommendContext.getRequest();
        List<CandidateItem> candidates = Lists.newLinkedList();
        int allItemNums = 0;
        HashMap<String, Queue<CandidateItem>> retrievalItemList = new HashMap<String, Queue<CandidateItem>>();

        List<Map.Entry<String, Float>> prioritySorted = new ArrayList<Map.Entry<String, Float>>(retrievalMapPriorityConf.entrySet());
        Collections.sort(prioritySorted, (o1, o2)-> o2.getValue().compareTo(o1.getValue()));
        log.info("/category:retrieval/prioritySorted:{}/device_id:{}/user_id:{}/request_id:{}",
                prioritySorted,request.getDeviceId(), request.getUserId(), request.getRequestId());

        for(Map.Entry<String, Float> m : prioritySorted) {
            if (!m.getKey().contains(DEFAULT_FINAL_RETRIEVAL) &&
                    retrievalList.get(m.getKey()) != null &&
                    retrievalList.get(m.getKey()).size() > 0) {

                candidates.addAll(
                        retrievalList.get(m.getKey()).stream()
                                .filter(e -> e.getFilterType() == CandidateItem.FilterType.NORMAL)
                                .filter(e -> !candidates.contains(e))
                                .limit(default_each_retrieval_num)
                                .collect(Collectors.toList()));

                int candidatesSize = candidates.size();
                Queue<CandidateItem> tmpItem = new LinkedList<>();
                tmpItem.addAll(candidates.subList(allItemNums, candidatesSize));
                retrievalItemList.put(m.getKey(),tmpItem);
                //log.info("/category:retrieval/prioritysum:{}/size:{}/size_source:{}/raw_size:{}/device_id:{}/user_id:{}/request_id:{}",
                //        prioritySum,candidatesSize-allItemNums, m.getKey(), retrievalList.get(m.getKey()).size(),request.getDeviceId(), request.getUserId(), request.getRequestId());

                allItemNums = candidatesSize;

            }
        }
        for(Map.Entry<String, Float> m : prioritySorted) {
            if (allItemNums >= DEFAULT_RANK_NUM) break;
            if (!m.getKey().contains(DEFAULT_FINAL_RETRIEVAL) &&
                    retrievalList.get(m.getKey()) != null &&
                    retrievalList.get(m.getKey()).size() > 0) {
                int itemNumbs = Math.toIntExact(
                        retrievalList.get(m.getKey()).stream()
                                .filter(e -> e.getFilterType() == CandidateItem.FilterType.NORMAL)
                                .count());
                candidates.addAll(
                        retrievalList.get(m.getKey()).stream()
                                .filter(e -> e.getFilterType() == CandidateItem.FilterType.NORMAL)
                                .filter(e -> !candidates.contains(e))
                                .limit(itemNumbs)
                                .collect(Collectors.toList()));
                int candidatesSize = candidates.size();
                Queue<CandidateItem> tmpItem = new LinkedList<>();
                tmpItem.addAll(candidates.subList(allItemNums, candidatesSize));
                tmpItem.addAll(retrievalItemList.get(m.getKey()));
                retrievalItemList.put(m.getKey(),tmpItem);
                //log.info("/category:retrieval/prioritysum:{}/size:{}/size_source:{}/raw_size:{}/device_id:{}/user_id:{}/request_id:{}",
                //        prioritySum,candidatesSize-allItemNums, m.getKey(), retrievalList.get(m.getKey()).size(),request.getDeviceId(), request.getUserId(), request.getRequestId());
                //log.info("/category:retrieval/prioritysum:{}/size:{}/size_source:{}/raw_size:{}/device_id:{}/user_id:{}/request_id:{}",
                //        prioritySum,retrievalItemList.get(m.getKey()).size(), m.getKey(), retrievalList.get(m.getKey()).size(),request.getDeviceId(), request.getUserId(), request.getRequestId());

                allItemNums = candidatesSize;
            }
        }
        for(Map.Entry<String, Queue<CandidateItem>> entry : retrievalItemList.entrySet()) {
            log.info("/category:retrieval/prioritysum:{}/size:{}/size_source:{}/raw_size:{}/device_id:{}/user_id:{}/request_id:{}",
                    prioritySum,entry.getValue().size(), entry.getKey(), retrievalList.get(entry.getKey()).size(),request.getDeviceId(), request.getUserId(), request.getRequestId());
        }
        // log.info("/category:retrieval/allItemNums:{}/device_id:{}/user_id:{}/request_id:{}",
        //         allItemNums, request.getDeviceId(), request.getUserId(), request.getRequestId());


        List<Item> finalCandidates = Lists.newLinkedList();
        // 会不会陷入死循环
        while(finalCandidates.size() < allItemNums){
            for(String i : retrievalItemList.keySet()){
                Item item = retrievalItemList.get(i).poll();
                if(item != null) {
                    finalCandidates.add(item);
                }
            }
        }

        if(allItemNums < DEFAULT_RANK_NUM
                && (retrievalList.get(DEFAULT_FINAL_RETRIEVAL) != null)
                && retrievalList.get(DEFAULT_FINAL_RETRIEVAL).size() > 0){
            int itemNumbs = Math.toIntExact(
                    retrievalList.get(DEFAULT_FINAL_RETRIEVAL).stream()
                            .filter(e -> e.getFilterType() == CandidateItem.FilterType.NORMAL)
                            .count());
            int allRecallSize = min(DEFAULT_RANK_NUM - allItemNums, itemNumbs);

            finalCandidates.addAll(
                    retrievalList.get(DEFAULT_FINAL_RETRIEVAL).stream()
                            .filter(e -> e.getFilterType() == CandidateItem.FilterType.NORMAL)
                            .filter(e -> !finalCandidates.contains(e))
                            .limit(allRecallSize)
                            .collect(Collectors.toList()));

            allItemNums += allRecallSize;
            log.info("/category:retrieval/merged size:{}/size:{}/size_source:{}/raw_size:{}/device_id:{}/user_id:{}/request_id:{}",
                    allItemNums, allRecallSize, "final_recall", itemNumbs, request.getDeviceId(), request.getUserId(), request.getRequestId());
        }

        if(allItemNums < 2 * recommendContext.getRequest().getNum()) {
            // 数量依然不够，用已读Item进行补充
            // log.info("/category:retrieval/item size is not enough/size:{}/device_id:{}/user_id:{}/request_id:{}",
            //        allItemNums, request.getDeviceId(), request.getUserId(), request.getRequestId());

            for (String i : retrievalList.keySet()) {
                if (allItemNums >= 2 * recommendContext.getRequest().getNum()) break;

                finalCandidates.addAll(
                        retrievalList.get(i).stream()
                                .filter(e -> (e.getFilterType() == CandidateItem.FilterType.READ || e.getFilterType() == CandidateItem.FilterType.SESSION))
                                .filter(e -> !finalCandidates.contains(e))
                                .limit(recommendContext.getRequest().getNum())
                                .collect(Collectors.toList()));
                allItemNums = finalCandidates.size();
            }
        }
        // log.info("/category:retrieval/merged size:{}/final size:{}/device_id:{}/user_id:{}/request_id:{}",
        //        allItemNums, finalCandidates.size(), request.getDeviceId(), request.getUserId(), request.getRequestId());
        // log.info("/category:retrieval/size:{}/size_source:all_retrievals/device_id:{}/user_id:{}/request_id:{}/cost_time_name:all_retrievals/cost_time:{}",
        //        candidates.size(), request.getDeviceId(), request.getUserId(), request.getRequestId(), sw.elapsed(TimeUnit.MILLISECONDS));

        return finalCandidates;
    }
}

package com.plato.recoserver.recoserver.core.retrieval;

import com.alibaba.hologres.client.model.Record;
import com.alibaba.hologres.com.google.common.collect.Maps;
import com.google.common.base.Stopwatch;
import com.plato.recoserver.recoserver.core.abtest.ABTestConfiguration;
import com.plato.recoserver.recoserver.core.abtest.ABTestProperty;
import com.plato.recoserver.recoserver.core.context.RecommendConfiguration;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.core.retrieval.inf.AbstractRetrieval;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lishuguang
 * @date 2022/7/26
 **/
@Slf4j
public class BasedRetrieval extends AbstractRetrieval {
    public BasedRetrieval(RetrievalConfig retrievalConfig) {
        super(retrievalConfig);
    }

    /**
     * 根据当前的推荐上下文从召回渠道获取指定数量的内容
     *
     * @param context 推荐上下文
     * @param quota    召回数量
     * @return 召回内容列表
     */
    @Override
    public List<CandidateItem> getCandidates(RecommendContext context, int quota) {
        Stopwatch sw = Stopwatch.createStarted();
        List<Long> rts = new LinkedList<>();
        Map<String, Integer> triggerMap = generateKeys(context, quota);
        if (MapUtils.isEmpty(triggerMap)) {
            return Collections.emptyList();
        }

        List<String> keys = new LinkedList<>();
        triggerMap.entrySet().forEach(e->{
            keys.add(e.getKey());
        });

        RetrievalConfig retrievalConfig = getRetrievalConfig();
        retrievalConfig.setRecommendContext(context);
        List<CandidateItem> candidateItems = getDataProvider().getData(keys, retrievalConfig);
        rts.add(sw.elapsed(TimeUnit.MILLISECONDS));

        if (CollectionUtils.size(candidateItems) == 0) {
            log.warn("/category:retrieval/all queues is empty with keys:{} of channel {}",
                    keys, getRetrievalConfig().getName());
            return Collections.emptyList();
        }
        if (CollectionUtils.size(keys) > 1){
            Collections.sort(candidateItems, Collections.reverseOrder(Comparator.comparing(CandidateItem::getScore)));
            TreeSet<CandidateItem> candidateItemTreeSet = new TreeSet<CandidateItem>((c1, c2) -> c1.equals(c2) ? 0 : c1.getScore() < c2.getScore() ? 1 : -1);
            candidateItemTreeSet.addAll(candidateItems);
            candidateItems = new LinkedList<>(candidateItemTreeSet);
        }
        rts.add(sw.elapsed(TimeUnit.MILLISECONDS));
        tagFilterType(candidateItems, context);
        rts.add(sw.elapsed(TimeUnit.MILLISECONDS));
        HashMap<String, Integer> triggerCounterMap = new HashMap<>(triggerMap);
        final List<CandidateItem> normalItems = new LinkedList<>();
        final List<CandidateItem> reservedFilteredItems = new LinkedList<>();
        final List<CandidateItem> reservedNormalItems = new LinkedList<>();
        final List<CandidateItem> result = new LinkedList<>();
        candidateItems.stream().forEach(e->{
            if (e.getFilterType() == CandidateItem.FilterType.NORMAL) {
                normalItems.add(e);
            } else if (e.getFilterType() != CandidateItem.FilterType.SPECIFIED){
                reservedFilteredItems.add(e);
            }
        });

        normalItems.stream().forEach(e->{
            if(triggerCounterMap.get(e.getTrigger()) > 0) {
                result.add(e);
                Integer counter = triggerCounterMap.get(e.getTrigger()) - 1;
                triggerCounterMap.put(e.getTrigger(), counter);
            } else {
                reservedNormalItems.add(e);
            }
        });
        if (CollectionUtils.size(result) < quota) {
            reservedNormalItems.stream().limit(quota-CollectionUtils.size(result)).forEach(result::add);
        }
        if (CollectionUtils.size(result) < quota) {
            reservedFilteredItems.stream().limit(quota-CollectionUtils.size(result)).forEach(result::add);
        }
        tagSourceName(result, getRetrievalConfig().getName());

        rts.add(sw.elapsed(TimeUnit.MILLISECONDS));
        log.info("/category:retrieval/retrieval info is as /original_size:{}, /result_size:{}, /filter_size:{}, /rts:{} of  /keys:{} and filter info {} with /device_id:{}, /user_id:{}, /request_id:{}/channel_name:{}/cost_time:{}",
                CollectionUtils.size(candidateItems), CollectionUtils.size(result), CollectionUtils.size(reservedFilteredItems),
                rts, keys, reservedFilteredItems.stream().collect(Collectors.groupingBy(CandidateItem::getFilterType, Collectors.counting())),
                context.getRequest().getDeviceId(), context.getRequest().getUserId(),context.getRequest().getRequestId(),getRetrievalConfig().getName(),
                sw.elapsed(TimeUnit.MILLISECONDS));

        return result;
    }

    public void tagFilterType(List<CandidateItem> list, RecommendContext context){
        // 过滤逻辑
        context.getReadFilter().filter(context, list);
        context.getRecHistoryFilter().filter(context, list);
        context.getSpecifiedFilterF().filter(context, list);
    }

    public void tagSourceName(List<CandidateItem> list, String name){
        list.parallelStream().forEach(e->e.setSource(name));
    }

    protected Map<String, Integer> generateKeys(RecommendContext context, int quota) {
        Map<String, Integer> keysMap = Maps.newHashMap();
        RecommendConfiguration.RetrievalConf retrievalConf = getRetrievalConfig();
        if(retrievalConf.getKeyValue() == null && retrievalConf.getKeyPrefix() == null) return keysMap;
        // priority : 1->2->3.1->3.2
        // 0、 临时增加
        if (StringUtils.equals(retrievalConf.getKeyPrefix(), "query") && StringUtils.equals(retrievalConf.getKeyValue(), "keyword") ) {
            if (CollectionUtils.isEmpty(context.getRtUserQueries())) return keysMap;
            context.getRtUserQueries()
                    .stream()
                    .map(e-> e.getObject(retrievalConf.getKeyValue()).toString())
                    .limit(2)
                    .forEach(e->keysMap.put(e,quota));
            return keysMap;
        }

        // 1、process single-trigger case
        if (retrievalConf.getKeyValue() == null){
            keysMap.put(retrievalConf.getKeyPrefix(), quota);
            return keysMap;
        }
        // 2、 process uid as value case
        if (Objects.equals(retrievalConf.getKeyValue(),"uid")) {
            RecoRequest request = context.getRequest();
            String uid = "";
            if (request.getUserId() > 0) {
                uid += request.getUserId();
            }else {
                uid += request.getDeviceId();
            }
            if(retrievalConf.getKeyPrefix() != null){
                keysMap.put(retrievalConf.getKeyPrefix() + uid, quota);
            }else {
                keysMap.put(uid, quota);
            }
            return keysMap;
        }
        // 3.1、process user rt profiles as value case
        if (CollectionUtils.size(context.getRtUserBehaviors()) > 0) {
            RecommendConfiguration.TriggerConf triggerConf = context.getRecommendConfiguration().getTriggerConf();
            // trigger实验****************
            ABTestProperty abTestProperty = context.getAbTestProperty();
            ABTestConfiguration abTestConfiguration = context.getAbTestConfiguration();
            if(MapUtils.isNotEmpty(abTestProperty.getPropertymap()) && abTestProperty.getPropertymap().get("trigger_select_exp") != null) {
                String group = abTestProperty.getPropertymap().get("trigger_select_exp").getGroup();
                if (StringUtils.equals(group, "trigger_select_a1")) {
                    RecommendConfiguration.TriggerConf triggerConfs = abTestConfiguration.getTriggerConf1();
                    triggerConf = triggerConfs;
                }else if (StringUtils.equals(group, "trigger_select_a2")) {
                    RecommendConfiguration.TriggerConf triggerConfs = abTestConfiguration.getTriggerConf2();
                    triggerConf = triggerConfs;
                }else if (StringUtils.equals(group, "trigger_select_a3")) {
                    RecommendConfiguration.TriggerConf triggerConfs = abTestConfiguration.getTriggerConf3();
                    triggerConf = triggerConfs;
                }
            }
            generateKeys(context.getRtUserBehaviors(), quota, keysMap, triggerConf);
        }

        // 3.2、process user profiles as value case
        // for a key, it will try to get value here only when get nothing from rt[3.1]
        Record record = context.getUserProfile();
        if (MapUtils.isEmpty(keysMap) &&
                context.getUserProfile() != null &&
                record.getSchema().getColumnIndex(retrievalConf.getKeyValue()) != null &&
                record.getObject(retrievalConf.getKeyValue()) != null) {
            keysMap.put(retrievalConf.getKeyPrefix() + record.getObject(retrievalConf.getKeyValue()), quota);
        }
        return keysMap;
    };

    protected void generateKeys(List<Record> records, int quota, Map<String, Integer> keysMap, RecommendConfiguration.TriggerConf triggerConf) {
        int diverseLimit = triggerConf.getDiversity() > 0 ? triggerConf.getDiversity() : 3;
        int maxLengthLimit = triggerConf.getLength() > 0 ? triggerConf.getLength() : 10;
        RecommendConfiguration.RetrievalConf retrievalConfig = getRetrievalConfig();
        Set<String> indicators = new HashSet<>();
        List<Record> candidateBehaviors = new LinkedList<>();
        String keyValue = retrievalConfig.getKeyValue();
        // 策略代码，后续考虑抽象出一个功能
        for (int i = 0; i < records.size(); i++){
            Record record = records.get(i);
            String indicator = null;
            if(CollectionUtils.size(indicators) > diverseLimit ) break;
            if (keyValue.equals("pid") && record.getObject("cate") != null){
                indicator = record.getObject("cate").toString();
            }else if (record.getObject(keyValue)!= null){
                indicator = record.getObject(keyValue).toString();
            }
            if(i < 3){
                candidateBehaviors.add(records.get(i));
            }else {
                if(indicator == null || indicators.contains(indicator)) continue;
                indicators.add(indicator);
                candidateBehaviors.add(records.get(i));
            }
        }
        Map<String, Long> keysDistribution = candidateBehaviors.
                stream().limit(maxLengthLimit)
                .filter(e->e.getObject(keyValue) != null)
                .collect(Collectors.groupingBy(e->e.getObject(keyValue).toString(),Collectors.counting()));
        Long base =  keysDistribution.values().stream().mapToLong(e->e).sum();
        keysDistribution.entrySet().stream().forEach(e->{
            int subQuota = (int) (e.getValue() * 1.0 * quota / base) + 1;
            if(retrievalConfig.getKeyPrefix() != null){
                keysMap.put(retrievalConfig.getKeyPrefix() + e.getKey(),subQuota);
            }else {
                keysMap.put(e.getKey(), subQuota);
            }
        });
    }
}

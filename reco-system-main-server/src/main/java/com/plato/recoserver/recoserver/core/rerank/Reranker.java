package com.plato.recoserver.recoserver.core.rerank;

import com.google.common.base.Stopwatch;
import com.plato.recoserver.recoserver.core.abtest.ABTestConfiguration;
import com.plato.recoserver.recoserver.core.abtest.ABTestProperty;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.rerank.strategy.StrategyManger;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.BreakInStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.ReWeightStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.RestrictStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IReranker;
import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.special.ArticleStrategy;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Reranker implements IReranker {
    @Autowired
    StrategyManger strategyManger;
    @Override
    public List<Item> rerank(RecommendContext context, List<Item> candidates) {
        return doStrategies(context, candidates);
    }

    public List<Item> doStrategies(RecommendContext context, List<Item> candidates){
        Stopwatch sw = Stopwatch.createStarted();
        List<IStrategy> strategies = strategyManger.getStrategies();

        ABTestConfiguration abTestConfiguration = context.getAbTestConfiguration();
        ABTestProperty abTestProperty = context.getAbTestProperty();
        if(abTestProperty.getPropertymap().get("recommendeds3support") != null) {
            String group = abTestProperty.getPropertymap().get("recommendeds3support").getGroup();
            if (!(StringUtils.equals(group, "recommneded3support_a1") || StringUtils.equals(group, "recommneded3support_a2"))) {
                List<ReWeightStrategy> reStrategies = abTestConfiguration.getReWeightStrategies();
                strategies.addAll(reStrategies);
                Collections.sort(strategies, Comparator.comparing(IStrategy::priority));
            }
        }
        if(abTestProperty.getPropertymap().get("gogoupcourse") != null) {
            String group = abTestProperty.getPropertymap().get("gogoupcourse").getGroup();
            if (StringUtils.equals(group, "gogoupcourse_b1")) {
                List<BreakInStrategy> reStrategies = abTestConfiguration.getBreakInStrategies();
                strategies.addAll(reStrategies);
                Collections.sort(strategies, Comparator.comparing(IStrategy::priority));
            }
        }

        if(abTestProperty.getPropertymap().get("article_block") != null) {
            String group = abTestProperty.getPropertymap().get("article_block").getGroup();
            if (StringUtils.equals(group, "restrict_article")) {
                List<RestrictStrategy> reStrategies = abTestConfiguration.getRestrictStrategies1();
                strategies.addAll(reStrategies);
                Collections.sort(strategies, Comparator.comparing(IStrategy::priority));
            } else if (StringUtils.equals(group, "free_throw")) {
                List<RestrictStrategy> reStrategies = abTestConfiguration.getRestrictStrategies2();
                strategies.addAll(reStrategies);
            } else {
                List<ArticleStrategy> reStrategies = abTestConfiguration.getRestrictStrategies();
                strategies.addAll(reStrategies);
            }
        }

        RecoRequest request = context.getRequest();
        List<Item> tunedList = candidates;
        for (IStrategy strategy : strategies){
            try {
                if(strategy.isCompletable()) tunedList = strategy.doTuner(context, tunedList);
            } catch (Exception e) {
                log.warn("/category:rerank/strategy the {} of strategies is failed in rerank for user {} with id {}, request {} ,e{}",
                        strategy.name(), request.getDeviceId(), request.getUserId(), request.getRequestId(), e);
            }
        }
        log.info("/category:rerank/device_id:{}/user_id:{}/request_id:{}/cost_time:{}/available_strategies:{}/all_strategies:{}/item_info:{}/cost_time_name:reranker",
                request.getDeviceId(), request.getUserId(), request.getRequestId(), sw.elapsed(TimeUnit.MILLISECONDS),
                strategies.stream().filter(IStrategy::isCompletable).map(IStrategy::name).collect(Collectors.toList()),
                strategies.stream().map(IStrategy::name).collect(Collectors.toList()), tunedList.stream().map(e->{
                    CandidateItem c = (CandidateItem) e;
                    if (StringUtils.isEmpty(c.getStrategy())) return null;
                    return c;
                }).filter(Objects::nonNull).collect(Collectors.toList()));
        return tunedList;
    }
}

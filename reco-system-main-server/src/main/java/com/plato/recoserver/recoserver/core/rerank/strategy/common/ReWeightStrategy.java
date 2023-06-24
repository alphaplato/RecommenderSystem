package com.plato.recoserver.recoserver.core.rerank.strategy.common;

import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IStrategy;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lishuguang
 * @date 2022/9/21
 **/
@Slf4j
public class ReWeightStrategy implements IStrategy {
    private Integer priority;
    private Double a;
    private Double b;
    private Double p;
    private String target;
    private String name;

    @Override
    public List<Item> doTuner(RecommendContext context, List<Item> candidates) {
        if(MapUtils.isEmpty(context.getItemProfiles())) return candidates;
        return reWeight(candidates);
    }

    @Override
    public Integer priority() {
        return priority;
    }

    @Override
    public String name() {
        return "Strategy_" + name;
    }

    @Override
    public boolean isCompletable() {
        if (a != null && b != null && p != null && target != null && priority != null){
            return true;
        }
        return false;
    }

    public List<Item> reWeight(List<Item> candidates) {
        List<Item> tunedList = new LinkedList<>(candidates);
        tunedList.forEach(e-> {
                    CandidateItem item = (CandidateItem) e;
                    if(StringUtils.equals(item.getSource(),target)) {
                        Double newScore = item.getRankScore() * (1 + (Math.pow(item.getScore(), p) * a + b));
                        item.setRankScore(newScore);
                        item.setStrategy(name());
                    }
        });
        log.info("/category:rerank/strategy items is re-weighted done after the {} completed", name );
        Collections.sort(tunedList, Comparator.comparingDouble(o-> {
            CandidateItem r = (CandidateItem) o;
            return r.getRankScore();
        }));
        Collections.reverse(tunedList);
        return tunedList;
    }
}

package com.plato.recoserver.recoserver.core.rerank.strategy.common;

import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IStrategy;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.util.WeightRandom;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.math3.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lishuguang
 * @date 2022/8/16
 **/
@Data
@Slf4j
public class BreakInStrategy implements IStrategy {
    private Integer position;
    private Integer priority;
    private String target;
    private String name;

    @Override
    public List<Item> doTuner(RecommendContext context, List<Item> candidates) {
        log.info("/category:rerank/strategy items size of break in source {} in rerank is {}", target, CollectionUtils.size(context.getRetrievalMap().get(target)));
        return breakIn(candidates, context.getRetrievalMap().get(target));
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
    public boolean isCompletable(){
        return target != null && priority != null && position >=0 ;
    }

    public List<Item> breakIn(List<Item> candidates, List<Item> breakInItems){
        if(CollectionUtils.size(candidates) < position || CollectionUtils.isEmpty(breakInItems)) {
            return candidates;
        }

        breakInItems = breakInItems.stream().filter(e ->
        {
            CandidateItem candidateItem = (CandidateItem) e;
            return candidateItem.getFilterType() == CandidateItem.FilterType.NORMAL;
        }).collect(Collectors.toCollection(LinkedList::new));
        if (CollectionUtils.isEmpty(breakInItems)) {
            return candidates;
        }

        Item itemSelected = select(breakInItems);
        log.info("/category:rerank/strategy items of break in after the {} completed in rerank is {}", name, itemSelected.type() + "_" + itemSelected.getId());
        int targetItemPos = candidates.indexOf(itemSelected);
        if(targetItemPos > position){
            CandidateItem item = (CandidateItem) candidates.get(targetItemPos);
            item.setStrategy(name());
            candidates.remove(targetItemPos);
            candidates.add(position, item);
            return candidates;
        }
        List<Item> tunedList = new LinkedList<>(candidates);
        tunedList.add(position,itemSelected);
        return tunedList;
    };

    private Item select(List<Item> breakInList){
        List<Pair<Item, Double>> itemScoreList = breakInList
                .stream()
                .map(e-> new Pair<>(e, e.getScore()))
                .collect(Collectors.toCollection(LinkedList::new));
        WeightRandom<Item, Double> weightRandom = new WeightRandom<>(itemScoreList);
        return weightRandom.random();
    }
}

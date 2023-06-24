package com.plato.recoserver.recoserver.core.rerank.strategy.special;

import com.alibaba.hologres.client.model.Record;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IStrategy;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.common.Item;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author lishuguang
 * @date 2022/11/17
 **/
public class ArticleStrategy implements IStrategy {
    private Integer priority;
    private String name;

    @Override
    public Integer priority() {
        return priority;
    }

    @Override
    public String name() {
        return "Strategy_" + name;
    }

    @Override
    public List<Item> doTuner(RecommendContext context, List<Item> candidates) {
        return restrict(candidates, context.getItemProfiles());
    }

    @Override
    public boolean isCompletable(){
        return priority != null;
    }

    public List<Item> restrict(List<Item> candidates, Map<Item, Record> itemRecordMap) {
        List<Item> tunedList = new LinkedList<>();
        List<Item> articleResidue = new LinkedList<>();
        for (Item candidate : candidates) {
            if ((   MapUtils.isEmpty(itemRecordMap)
                    || itemRecordMap.get(candidate) == null
                    || itemRecordMap.get(candidate).getObject("item_recommend") == null
                    || StringUtils.equals(itemRecordMap.get(candidate).getObject("item_recommend").toString(), "0"))
                    && candidate.type() == 2) {
                CandidateItem c = (CandidateItem) candidate;
                c.setStrategy(name());
                articleResidue.add(c);
            } else {
                tunedList.add(candidate);;
            }
        }
        tunedList.addAll(articleResidue);
        return tunedList;
    }
}

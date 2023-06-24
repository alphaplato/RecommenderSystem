package com.plato.recoserver.recoserver.core.rerank.strategy.inf;

import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.RecommendContext;

import java.util.List;

/**
 * @author lishuguang
 * @date 2022/8/16
 **/
public interface IStrategy {
    List<Item> doTuner(RecommendContext context, List<Item> Candidates);

    Integer priority();

    String name();

    boolean isCompletable();
}

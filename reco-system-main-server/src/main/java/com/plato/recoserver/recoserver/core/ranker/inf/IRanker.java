package com.plato.recoserver.recoserver.core.ranker.inf;

import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.RecommendContext;

import java.util.List;

/**
 * @author Kevin
 * @date 2022-03-21
 */
public interface IRanker {

    /**
     * rank the retrieved items for the user
     * @param context recommendation context
     * @param items the item list need to rank
     * @return the ranked item lis order by socre
     */
    List<Item> rank(RecommendContext context, List<Item> items);
}

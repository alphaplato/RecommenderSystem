package com.plato.recoserver.recoserver.core.rerank.strategy.inf;

import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.RecommendContext;

import java.util.List;

/**
 * @author songruixue
 * @date 2022-06-30
 */
public interface IReranker {
    List<Item> rerank(RecommendContext context, List<Item> candidates);
}

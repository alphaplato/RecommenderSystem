package com.plato.recoserver.recoserver.core.retrieval.filter.inf;

import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.common.CandidateItem;

import java.util.List;


public interface IFilter {
    /**
     * 过滤函数
     */
    void filter(RecommendContext context, List<CandidateItem> items);
}

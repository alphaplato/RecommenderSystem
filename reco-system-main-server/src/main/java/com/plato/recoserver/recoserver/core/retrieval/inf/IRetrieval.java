package com.plato.recoserver.recoserver.core.retrieval.inf;

import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.retrieval.RetrievalConfig;
import com.plato.recoserver.recoserver.common.CandidateItem;

import java.util.List;

/**
 * 召回渠道
 * @author Kevin
 * @date 2022-03-07
 */
public interface IRetrieval {

    /**
     * 召回渠道名
     * @return 该召回渠道的名字
     */
    String name();

    /**
     * 根据当前的推荐上下文从召回渠道获取指定数量的内容
     * @param context 推荐上下文
     * @param size 召回数量
     * @return 召回内容列表
     */
    List<CandidateItem> getCandidates(RecommendContext context, int size);

    /**
     * 召回渠道的配置
     * @return
     */
    RetrievalConfig getRetrievalConfig();

    /**
     * 该召回渠道的数据源
     * @return
     */
    IRetrievalDataProvider getDataProvider();

}
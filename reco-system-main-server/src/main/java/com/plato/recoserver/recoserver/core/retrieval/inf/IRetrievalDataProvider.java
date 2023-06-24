package com.plato.recoserver.recoserver.core.retrieval.inf;

import com.plato.recoserver.recoserver.core.context.RecommendConfiguration;
import com.plato.recoserver.recoserver.common.Item;

import java.util.List;

/**
 * @author Kevin
 * @date 2022-03-11
 */
public interface IRetrievalDataProvider<T extends Item> {

    /**
     * 从召回数据源获取原始的推荐列表，未来拆分 getData 为 rpc server
     * @param key
     * @return
     */
    List<T> getData(String key, RecommendConfiguration.RetrievalConf retrievalConf);

    List<T> getData(List<String> keys, RecommendConfiguration.RetrievalConf retrievalConf);
}

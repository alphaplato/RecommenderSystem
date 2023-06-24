package com.plato.recoserver.recoserver.core.retrieval.inf;

import com.plato.recoserver.recoserver.core.retrieval.RetrievalConfig;
import com.plato.recoserver.recoserver.common.Item;
import lombok.Data;

/**
 * @author Kevin
 * @date 2022-03-11
 */
@Data
public abstract class AbstractRetrievalDataProvider<T extends Item> implements IRetrievalDataProvider<T> {
    private RetrievalConfig retrievalConfig;
}
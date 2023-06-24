package com.plato.recoserver.recoserver.core.retrieval.inf;

import com.plato.recoserver.recoserver.core.retrieval.RetrievalConfig;

/**
 * @author Kevin
 * @date 2022-03-14
 */
public abstract class AbstractRetrieval implements IRetrieval {

    /**
     * 召回渠道的配置
     */
    private RetrievalConfig retrievalConfig;

    /**
     * 召回数据的提供源
     */
    private IRetrievalDataProvider dataProvider;

    public AbstractRetrieval(RetrievalConfig retrievalConfig) {
        this.retrievalConfig = retrievalConfig;
    }

    @Override
    public RetrievalConfig getRetrievalConfig() {
        return this.retrievalConfig;
    }

    @Override
    public String name() {
        return retrievalConfig.getName();
    }

    @Override
    public IRetrievalDataProvider getDataProvider() {
        return this.dataProvider;
    }

    public void setDataProvider(IRetrievalDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
}

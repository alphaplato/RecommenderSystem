package com.plato.recoserver.recoserver.core.context.builder.inf;

import com.plato.recoserver.recoserver.core.context.RecommendContext;

/**
 * @author Kevin
 * @date 2022-04-06
 */
public interface IRecommendCxtBuilder {

    String name();

    void build(RecommendContext recommendContext);

}

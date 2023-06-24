package com.plato.recoserver.recoserver.core.retrieval;

import com.plato.recoserver.recoserver.core.context.RecommendConfiguration;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Kevin
 * @date 2022-03-11
 */
@Getter
@Setter
public class RetrievalConfig extends RecommendConfiguration.RetrievalConf {
    private RecommendContext recommendContext;
}

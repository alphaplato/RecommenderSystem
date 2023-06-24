package com.plato.recoserver.recoserver.core.rerank.strategy;

import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.BreakInStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.ScatterStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.special.FolderStrategy;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lishuguang
 * @date 2022/8/19
 **/
@Component
@Data
public class StrategyManger {
    public List<IStrategy> getStrategies(){
        List<IStrategy> strategies = new LinkedList<>();
        strategies.addAll(scatterStrategies);
        strategies.addAll(breakInStrategies);
        strategies.addAll(folderStrategies);
        strategies = strategies.stream()
                .filter(Objects::nonNull)
                .filter(IStrategy::isCompletable)
                .sorted(Comparator.comparing(IStrategy::priority))
                .collect(Collectors.toCollection(LinkedList::new));
        return strategies;
    }

    @ApolloJsonValue("${reco.server.strategy.scatter:[]}")
    private List<ScatterStrategy> scatterStrategies;

    @ApolloJsonValue("${reco.server.strategy.breakin:[]}")
    private List<BreakInStrategy> breakInStrategies;

    @ApolloJsonValue("${reco.server.strategy.folder:[]}")
    private List<FolderStrategy> folderStrategies;

}

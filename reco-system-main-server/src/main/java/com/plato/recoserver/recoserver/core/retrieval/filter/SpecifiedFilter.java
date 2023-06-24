package com.plato.recoserver.recoserver.core.retrieval.filter;

import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.core.retrieval.filter.inf.IFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author maxiangzhen
 * @date 2022-10-10
 */
@Slf4j
@Component
public class SpecifiedFilter implements IFilter {
    @Override
    public void filter(RecommendContext context, List<CandidateItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }

        Set<Integer> integerSet = context.getSpecifiedFilter()
                .stream()
                .filter(e-> ((Long) e.getId() == 0))
                .map(e->e.type()).collect(Collectors.toCollection(HashSet::new));

        items.parallelStream().forEach(e->{
            if(context.getSpecifiedFilter().contains(e) || integerSet.contains(e.type())) {
                e.setFilterType(CandidateItem.FilterType.SPECIFIED);
            }
        });
    }


}

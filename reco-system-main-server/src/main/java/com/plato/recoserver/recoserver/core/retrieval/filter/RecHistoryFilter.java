package com.plato.recoserver.recoserver.core.retrieval.filter;

import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.core.retrieval.filter.inf.IFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author maxiangzhen
 * @date 2022-10-10
 */
@Slf4j
@Component
public class RecHistoryFilter implements IFilter {
    @Override
    public void filter(RecommendContext context, List<CandidateItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }

        items.parallelStream().forEach( e->{
            if(context.getRecHistory().contains(e)) {
                e.setFilterType(CandidateItem.FilterType.SESSION);
            }
        });
    }

}

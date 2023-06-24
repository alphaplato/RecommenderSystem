package com.plato.recoserver.recoserver.core.retrieval.filter;

import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.service.TairBloomService;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.core.retrieval.filter.inf.IFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author maxiangzhen
 * @date 2022-10-10
 **/
@Slf4j
@Service
public class ReadFilter implements IFilter {
    @Autowired
    TairBloomService tairBloomService;
    @Override
    public void filter(RecommendContext context, List<CandidateItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        String keyStr = "rf_" + context.getRequest().getUserId();
        tairBloomService.multiFilterRead(keyStr, items);
    }
}


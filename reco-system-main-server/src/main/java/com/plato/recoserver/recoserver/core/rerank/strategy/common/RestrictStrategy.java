package com.plato.recoserver.recoserver.core.rerank.strategy.common;

import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IStrategy;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lishuguang
 * @date 2022/11/15
 **/
@Data
public class RestrictStrategy implements IStrategy {
    // 有限按照 ratio 的请求比例，再按照 num
    private Double ratio;
    private Integer priority;
    private String name;
    private String types;

    @Override
    public Integer priority() {
        return priority;
    }

    @Override
    public String name() {
        return "Strategy_" + name;
    }

    @Override
    public List<Item> doTuner(RecommendContext context, List<Item> candidates) {
        if(ratio == null) return candidates;
        int num =  (int) (context.getRequest().getNum() * ratio);
        return restrict(candidates ,num);
    }

    private Set<Integer> parseType(String types){
        return Arrays.stream(StringUtils.split(types,',')).map(e->{
            try {
                return Integer.valueOf(e);
            } catch (Exception ignored){
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toCollection(HashSet::new));
    };

    @Override
    public boolean isCompletable(){
        return ratio != null  && priority != null && StringUtils.isNotEmpty(types);
    }

    public List<Item> restrict(List<Item> candidates, int num) {
        List<Item> tunedList = new LinkedList<>();
        List<Item> residueList = new LinkedList<>();
        Set<Integer> ts = parseType(types);
        if(CollectionUtils.isEmpty(ts)) return candidates;
        int counter = 0;
        for (Item candidate : candidates) {
            if (!ts.contains(candidate.type())) {
                tunedList.add(candidate);
            } else {
                if (counter < num) {
                    tunedList.add(candidate);
                    counter ++;
                } else {
                    residueList.add(candidate);
                }
            }
        }
        tunedList.addAll(residueList);
        return tunedList;
    }
}

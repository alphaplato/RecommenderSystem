package com.plato.recoserver.recoserver.core.rerank.strategy.special;

import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IStrategy;
import com.plato.recoserver.recoserver.common.Item;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author lishuguang
 * @date 2022/8/19
 **/
@Data
public class FolderStrategy implements IStrategy {
    // 有限按照 ratio 的请求比例，再按照 num
    private Double ratio;
    private Integer priority;
    private String name;

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
        if(num <= 0) num = 1;
        return restrict(candidates, num);
    }

    @Override
    public boolean isCompletable(){
        return ratio != null  && priority != null;
    }

    public List<Item> restrict(List<Item> candidates, int num) {
        List<Item> tunedList = new LinkedList<>();
        List<Item> folderResidue = new LinkedList<>();
        int counter = 0;
        for (Item candidate : candidates) {
            if (counter < num) {
                tunedList.add(candidate);
            } else {
                if (candidate.type() == 3 || candidate.type() == 4 || candidate.type() == 6) {
                    folderResidue.add(candidate);
                } else {
                    tunedList.add(candidate);
                }
            }
            if (candidate.type() == 3 || candidate.type() == 4 || candidate.type() == 6) counter++;
        }
        tunedList.addAll(folderResidue);
        return tunedList;
    }
}

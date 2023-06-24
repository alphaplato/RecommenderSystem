package com.plato.recoserver.recoserver.core.rerank.strategy.common;

import com.alibaba.hologres.client.model.Record;
import com.plato.recoserver.recoserver.core.rerank.strategy.inf.IStrategy;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lishuguang
 * @date 2022/8/16
 **/
// 基础的打散功能：窗口打散
@Data
public class ScatterStrategy implements IStrategy {
    private String target;
    private Integer windowSize;
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
        if(MapUtils.isEmpty(context.getItemProfiles())) return candidates;
        return scatter(context.getItemProfiles(), candidates);
    }

    @Override
    public boolean isCompletable(){
        return target != null && windowSize != null && priority != null;
    }

    public List<Item> scatter(Map<Item, Record> itemProfiles, List<Item> candidates){
        LinkedList<Object> targets = new LinkedList<>();
        List<Item> tunedList = new LinkedList<>(candidates);
        int minWindowSize = windowSize;

        int length = tunedList.size();
        for(int i = 0; i < length; i++){
            if (getObject(itemProfiles, tunedList.get(i), target) == null) continue;

            minWindowSize = adjustWindowSize(i, candidates, itemProfiles);
            if(minWindowSize <= 0) break;

            while (CollectionUtils.size(targets) >= minWindowSize) {
                targets.removeFirst();
            }
            if (!targets.contains(getObject(itemProfiles, tunedList.get(i), target))) {
                targets.add(getObject(itemProfiles, tunedList.get(i), target));
                continue;
            }
            int j = i + 1;
            while(j < length){
                if (!targets.contains(getObject(itemProfiles, tunedList.get(j), target))){
                    targets.add(getObject(itemProfiles, tunedList.get(j), target));
                    Item item = tunedList.get(j);
                    tunedList.remove(j);
                    tunedList.add(i, item);
                    break;
                };
                j++;
            };
        }
        return tunedList;
    };

    private int adjustWindowSize(int pos, List<Item> list,  Map<Item, Record> itemProfiles){
        if(pos >= CollectionUtils.size(list)) return 0;
        Set<?> targetSet = list.subList(pos, CollectionUtils.size(list))
                .stream().map(e-> getObject(itemProfiles, e, target))
                .collect(Collectors.toCollection(HashSet::new));
        return windowSize > CollectionUtils.size(targetSet) ? CollectionUtils.size(targetSet) : windowSize;
    }

    public Object getObject( Map<Item, Record> itemProfiles, Item item, String target){
        if (!itemProfiles.containsKey(item)) return null;
        if (itemProfiles.get(item).getObject(target) == null) return null;
        return itemProfiles.get(item).getObject(target);
    }
}

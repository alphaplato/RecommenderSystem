package com.plato.recoserver.recoserver.util;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author lishuguang
 * @date 2022/8/17
 **/
public class WeightRandom<K,V extends Number> {
    private TreeMap<Double, K> weightMap = new TreeMap<Double, K>();

    public WeightRandom(List<Pair<K, V>> list) {
        Preconditions.checkNotNull(list, "list can NOT be null!");
        for (Pair<K, V> pair : list) {
            Preconditions.checkArgument(pair.getValue().doubleValue() > 0, String.format("非法权重值：pair=%s", pair));

            double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey();//统一转为double
            this.weightMap.put(pair.getValue().doubleValue() + lastWeight, pair.getKey());//权重累加
        }
    }

    public K random() {
        double randomWeight = this.weightMap.lastKey() * Math.random();
        SortedMap<Double, K> tailMap = this.weightMap.tailMap(randomWeight, false);
        return this.weightMap.get(tailMap.firstKey());
    }
}
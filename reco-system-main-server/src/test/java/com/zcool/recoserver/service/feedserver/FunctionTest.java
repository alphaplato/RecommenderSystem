package com.zcool.recoserver.service.feedserver;

/**
 * @author lishuguang
 * @date 2022/6/27
 **/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.plato.recoserver.recoserver.core.abtest.ABTestProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 测试一些基础函数等，不属于任何单元模块测试
**/
public class FunctionTest {
    @Test
    public void JsonParse(){
        String str = "{\"works\":[{\"type\":3,\"id\":\"11874531\"},{\"type\":8,\"id\":\"294412\"},{\"type\":28,\"id\":\"8518925\"},{\"type\":29,\"id\":\"463\"},{\"type\":29,\"id\":\"464\"}],\"requestId\":\"110a0d156b2e40d7b173b3156f59797e\",\"version\":\"1.0\",\"memberId\":\"22137894\",\"sendTime\":\"1654155859134\"}";
        JSONArray jsonArray = JSON.parseObject(str).getJSONArray("works");
        jsonArray.stream().forEach(e->System.out.println(((JSONObject)e).getIntValue("id")));
        System.out.println(jsonArray.getClass());
        List<Object> stringSet = new LinkedList<>();
        stringSet.add(null);
        stringSet.add(1);
        stringSet.add("3");
        String a=null;
        String s="1";
        String[] t = s.split(":",10);
//        System.out.println(t.length);
        stringSet.add(1,1111);
//        System.out.println(Objects.equals(s,a));
        List<Double> d = new LinkedList<>();
        d.add(0.1);
        d.add(0.3);
        Collections.sort(d,Comparator.comparingDouble(o->o));
        Collections.reverse(d);
        HashMap<String,Integer> m = new HashMap<>();
        ABTestProperty abTestProperty  = new ABTestProperty();
        String ss = ":0,1:,1:0";
        System.out.println(parseRestrictions(ss));
//        Object o = stringSet.g
//        et(1);
//        stringSet.remove(1);
//        System.out.println(o);
//        System.out.println(stringSet.indexOf("4"));
//        stringSet = null;
//        stringSet.stream().collect(Collectors.toCollection(LinkedList::new));
    }

    private Map<Integer, Double> parseRestrictions(String restrictions){
        return Arrays.stream(StringUtils.split(restrictions,',')).map(e->{
            List<String> r = Arrays.asList(StringUtils.split(e, ':'));
            if(CollectionUtils.size(r) != 2) {
                return null;
            }
            try {
                Integer k = Integer.parseInt(r.get(0));
                Double v = Double.valueOf(r.get(1));
                return new Pair<>(k, v);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}

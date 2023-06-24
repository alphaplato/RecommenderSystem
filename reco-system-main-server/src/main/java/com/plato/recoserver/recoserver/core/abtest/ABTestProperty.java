package com.plato.recoserver.recoserver.core.abtest;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kevin
 * @date 2022-04-13
 */
//@Getter
//@Setter
//public class ABTestProperty {
//    private String id;
//    private String value;
//    private Set<Integer> flowIndex;
//}
@Getter
@Setter
@Slf4j
public class ABTestProperty {
    private Map<String, Property> propertymap = Collections.emptyMap();
    @Getter
    @Setter
    public static class Property {
        private String group;
        private int flow;
    }
    public String getAbGroups(){
        if (MapUtils.isEmpty(propertymap)){
            return "";
        }
        try {
            LinkedList<String> abBuckets = propertymap.entrySet()
                    .stream()
                    .map(e -> e.getKey() + ":" + e.getValue().getGroup())
                    .collect(Collectors.toCollection(LinkedList::new));
            return String.join("|",abBuckets);
        } catch (Exception e){
            log.warn("ab config is error, please reset it!");
        }
        return "";
    }
}
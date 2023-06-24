package com.plato.recoserver.recoserver.core.service;

import com.google.common.collect.Lists;
import com.plato.recoserver.recoserver.datamanager.dao.redis.JedisClient;
import com.plato.recoserver.recoserver.common.CandidateItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Kevin
 * @date 2022-03-25
 */
@Slf4j
@Service
public class TairBloomService {

    @Autowired
    private JedisClient tairbloomJedis;

    @Autowired
    private ThreadPoolTaskExecutor tairBloomExecutor;

    int singleListLength = 2000;

    /**
     * remove the item which is already read in the item list
     * @param items the list of items to do the read filter
     */
    public void filterRead(String keyStr, List<CandidateItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        //rf=read filter
        byte[] key = keyStr.getBytes(StandardCharsets.UTF_8);
        byte[][] values = items.stream()
                .map(e -> e.type() + "_" + e.getId())
                .map(e -> e.getBytes(StandardCharsets.UTF_8))
                .toArray(byte[][]::new);
        Boolean[] readOrNot = tairbloomJedis.executeBloomR(tairBloom -> tairBloom.bfmexists(key, values));
        // log.info("uid is {}, key is {}, readOrNot is {}", keyStr, key, readOrNot);
        Iterator<CandidateItem> iterator = items.iterator();
        int i = 0;
        while (iterator.hasNext() && i < readOrNot.length) {
            CandidateItem item = iterator.next();
            if (readOrNot[i]) {
                 item.setFilterType(CandidateItem.FilterType.READ);
            }
            i++;
        }
    }

    public void multiFilterRead(String keyStr, List<CandidateItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        //rf=read filter
        List<List<CandidateItem>> itemLists = Lists.partition(items, singleListLength);
        List<Future> futures = new LinkedList<>();
        for (List<CandidateItem> list : itemLists){
            Future future = tairBloomExecutor.submit(() -> filterRead(keyStr, list));
            futures.add(future);
        }
        Iterator<Future> iterator = futures.iterator();
        while (iterator.hasNext()){
            Future future = iterator.next();
            try {
                future.get();
            } catch (Exception e) {
                log.warn("/category:read/warn_name: exists one bloom to get failed");
            }
        }
    }
}

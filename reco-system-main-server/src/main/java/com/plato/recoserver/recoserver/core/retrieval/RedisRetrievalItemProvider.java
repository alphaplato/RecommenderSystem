package com.plato.recoserver.recoserver.core.retrieval;

import com.plato.recoserver.recoserver.core.context.RecommendConfiguration;
import com.plato.recoserver.recoserver.datamanager.dao.redis.JedisClient;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.core.retrieval.inf.AbstractRetrievalDataProvider;
import com.plato.recoserver.recoserver.util.RecUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import redis.clients.jedis.PipelineBase;
import redis.clients.jedis.Response;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Redis基础的
 * @author Kevin
 * @date 2022-03-11
 */
@Slf4j
public class RedisRetrievalItemProvider extends AbstractRetrievalDataProvider<CandidateItem> {

    private final static int RETRIEVAL_ITEM_BYTES = 18;

    private JedisClient retrievalJedis;

    public RedisRetrievalItemProvider(JedisClient retrievalJedis) {
        this.retrievalJedis = retrievalJedis;
    }

    @Override
    public List<CandidateItem> getData(String key, RecommendConfiguration.RetrievalConf retrievalConf) {
        final byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = retrievalJedis.execute(jedis -> jedis.get(keyBytes));
        if (null == valueBytes || valueBytes.length == 0) {
            log.warn("/category:retrieval/warn_name:retrieval-cannot get any data from redis/key:{}", key);
            return Collections.emptyList();
        }
        return bytesToItems(valueBytes, key);
    }

    @Override
    public List<CandidateItem> getData(List<String> keys, RecommendConfiguration.RetrievalConf retrievalConf) {
        final List<byte []> keysBytes = keys.stream().
                map(e->e.getBytes(StandardCharsets.UTF_8)).
                collect(Collectors.toCollection(LinkedList::new));
        final List<Response<byte []>> responseList = retrievalJedis.executeRPipeline(keysBytes, PipelineBase::get);
        List<CandidateItem> candidateItems = new LinkedList<>();
        for (int i = 0; i < CollectionUtils.size(keys); i++){
            String key = keys.get(i);
            byte[] valueBytes = responseList.get(i).get();
            if (null == valueBytes || valueBytes.length == 0) {
                continue;
            }
            candidateItems.addAll(bytesToItems(valueBytes, key));
        }
        return candidateItems;
    }

    private List<CandidateItem> bytesToItems(byte[] valueBytes, String key){
        valueBytes = Base64.getDecoder().decode(valueBytes);
        if (valueBytes.length % RETRIEVAL_ITEM_BYTES != 0) {
            //如果总的字节数不能整除type+id+score的字节数，则说明数据有问题，但后续仍然尝试去解析
            log.warn("/category:retrieval/warn_name:retrieval-total bytes received maybe corrupt, will try to decode it!/size:{}",
                    valueBytes.length);
        }
        int count = valueBytes.length / RETRIEVAL_ITEM_BYTES;
        List<CandidateItem> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            int start = i * RETRIEVAL_ITEM_BYTES;
            int type = RecUtils.bytesToShort(valueBytes, start);
            long id = RecUtils.bytesToLong(valueBytes, start + 2);
            double score = RecUtils.bytesToDouble(valueBytes, start + 10);
            result.add(new CandidateItem(id, type, score, key));
        }
        return result;
    }
}

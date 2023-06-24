package com.plato.recoserver.recoserver.core.retrieval;

import com.alibaba.hologres.client.Get;
import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.client.model.Record;
import com.alibaba.hologres.client.model.TableSchema;
import com.alibaba.hologres.org.postgresql.jdbc.PgArray;
import com.plato.recoserver.recoserver.core.context.RecommendConfiguration;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.core.retrieval.inf.AbstractRetrievalDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lishuguang
 * @date 2022/8/1
 **/
@Slf4j
public class HoloRetrievalItemProvider extends AbstractRetrievalDataProvider<CandidateItem> {
    private final static int DEFAULT_RECALL_NUM = 300;
    private HoloClient holoClient;

    public HoloRetrievalItemProvider(HoloClient holoClient) {
        this.holoClient = holoClient;
    }

    @Override
    public List<CandidateItem> getData(String key, RecommendConfiguration.RetrievalConf retrievalConf) {
        return getResult(key, retrievalConf);
    }

    @Override
    public List<CandidateItem> getData(List<String> keys, RecommendConfiguration.RetrievalConf retrievalConf) {
        List<CandidateItem> result = new LinkedList<>();
        keys.stream()
                .map(e->getResult(e,retrievalConf))
                .forEach(result::addAll);
        return result;
    }

    public Object getKeyEmbedding(String key, RecommendConfiguration.RetrievalConf retrievalConf) {
        String[] srcTable = retrievalConf.getSrcTable().split(":",10);
        if(srcTable.length != 2) return null;
        try {
            TableSchema table = holoClient.getTableSchema(StringUtils.trim(srcTable[0]));
            Get get = Get.newBuilder(table).setPrimaryKey(StringUtils.trim(srcTable[1]), key).build();
            Record user = holoClient.get(get).get();
            if (user == null) {
                log.warn("/category:retrieval/warn_name:get no user data from holo/key:{}/table:{}", key, srcTable);
                return null;
            }
            return user.getObject("embedding");
        } catch (Exception e){
            log.warn("/category:retrieval/warn_name:get user data failed from holo/key:{}/table:{}, e:{}", key, srcTable, e);
            return null;
        }
    }

    public List<CandidateItem> getResult(String key, RecommendConfiguration.RetrievalConf retrievalConf) {
        List<CandidateItem> result = new LinkedList<>();
        Object userEmbedding = getKeyEmbedding(key, retrievalConf);
        if(userEmbedding == null) return result;
        String[] desTable = retrievalConf.getDesTable().split(":",10);
        if(desTable.length != 2) return result;
        try {
            holoClient.sql(conn -> {
                String sql = String.format("select %s,pm_approx_inner_product_distance(embedding, ?) as distance from %s order by distance desc limit ?", desTable[1], desTable[0]);
                PreparedStatement st = conn.prepareStatement(sql);
                st.setArray(1, convert(userEmbedding, conn));
                st.setInt(2, DEFAULT_RECALL_NUM);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    String[] pid = rs.getString(desTable[1]).split("_");
                    if(pid.length != 2) {
                        continue;
                    }
                    double score = rs.getDouble("distance");
                    result.add(new CandidateItem(Long.parseLong(pid[1]), Short.parseShort(pid[0]), score, key));
                }
                rs.close();
                return true;
            }).get();
        } catch (Exception e) {
            log.warn("/category:retrieval/warn_name:retrieval-cannot get any items from holo/key:{}/table:{}, e:{}", key, desTable, e);
        }
        return result;
    }

    public PgArray convert(Object o, Connection conn) throws SQLException {
        return (PgArray) o;
    }
}

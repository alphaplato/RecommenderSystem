package com.zcool.recoserver.service.feedserver;

import com.alibaba.hologres.client.Get;
import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.client.exception.HoloClientException;
import com.alibaba.hologres.client.model.Record;
import com.alibaba.hologres.client.model.TableSchema;
import com.alibaba.hologres.org.postgresql.jdbc.PgArray;
import com.google.common.collect.Lists;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.configure.CloudConfigConfiguration;
import com.plato.recoserver.recoserver.configure.RecoServerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @author songruixue
 * @date 2022/7/21
 **/
@SpringBootTest(properties = {
        "apollo.meta=http://apolloconfig-dev.in.zcool.cn/config",
        "hologres.client.profile.url=jdbc:postgresql://hgprecn-cn-2r42ppl1q001-cn-beijing.hologres.aliyuncs.com:80/profile_service",
        "hologres.client.profile.username=LTAI5tKwmYTF8EhrpgtTcdSt",
        "hologres.client.profile.password=bCNsmidzs9028bATP1CXZj5zn3PwI3",
        "hologres.client.profile.threadsize=3",
        "hologres.client.profile.batchsize=256",
        "hologres.client.profile.queuesize=128",
        "redis.client.host=r-2zem0286ielmbanc83pd.redis.rds.aliyuncs.com:6379",
        "redis.client.password=^!jmc7Y3B91kFdIN",
        "redis.client.db=0"
})

@SpringJUnitConfig({RecoServerConfiguration.class, CloudConfigConfiguration.class})
@DirtiesContext
@EnableConfigurationProperties
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@Component
public class VecRetrievalHoloTest {
    final int DEFAULT_RECALL_NUM = 100;
    @Autowired
    private HoloClient client;
    Record user;
    @Test
    public void readData() throws Exception {
        TableSchema schema = client.getTableSchema("dssm_recall_user_embedding_holo_v1");
        System.out.printf("schema: %s\n", schema);
        String user_id = "15562572";
        Object userEmbedding = null;
        try{
            userEmbedding = getUserEmbedding(schema,user_id);
        } catch(NullPointerException e){
            log.println("non userEmbedding");
            return ;
        }
        List<CandidateItem> candidates = new LinkedList<>();
        try {
            candidates = getCandidates(userEmbedding);
        } catch (NullPointerException e){
            log.println(e);
            return ;
        }
        candidates.parallelStream().forEach(e->{
            e.setFilterType(CandidateItem.FilterType.NORMAL);
        });

//        List<List<CandidateItem>> cc = Lists.partition(candidates, 2);
//        cc.get(0).get(0).setFilterType(CandidateItem.FilterType.READ);

        CandidateItem a = new CandidateItem(123L, 1, 11117.0);
        CandidateItem b = new CandidateItem(123L, 1, 11115.0);
        candidates.add(a);
        candidates.add(b);
        TreeSet<CandidateItem> candidateItemTreeSet = new TreeSet<CandidateItem>((c1,  c2)-> c1.equals(c2) && c1.getScore() == c2.getScore() ? 0 : c1.getScore() < c2.getScore() ? 1 : -1);
        candidateItemTreeSet.addAll(candidates);
        TreeSet<CandidateItem> candidateItemTreeSet2 = new TreeSet<CandidateItem>((c1, c2) -> c1.equals(c2) ? 0 : c1.getScore() < c2.getScore() ? 1 : -1);
        candidateItemTreeSet2.addAll(candidateItemTreeSet);
        System.out.println(new LinkedList<>(candidateItemTreeSet2));

    }
    public Object getUserEmbedding (TableSchema schema ,String user_id) throws SQLException, HoloClientException, ExecutionException, InterruptedException {

        Get get = Get.newBuilder(schema).setPrimaryKey("user_id", user_id).build();
        user =  client.get(get).get();
        return user.getObject("embedding");
    }
    public List<CandidateItem> getCandidates(Object userEmbedding)  {

        List<CandidateItem> candidates = Lists.newLinkedList();
        try {
            client.sql(conn -> {
            String sql = String.format("select %s,pm_approx_inner_product_distance(embedding, ?) as distance from %s order by distance desc limit ?","item_id", "dssm_recall_item_embedding_holo_v1");
            PreparedStatement st = conn.prepareStatement(sql);
            System.out.println(userEmbedding.getClass());
//            st.setString(1,userEmbedding.toString());
            st.setArray(1, (PgArray) userEmbedding);
            st.setInt(2, DEFAULT_RECALL_NUM);
            System.out.println(st);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String[] pid = rs.getString("item_id").split("_");
                if(pid.length != 2) {
                    continue;
                }
                double score = rs.getDouble("distance");
                candidates.add(new CandidateItem(Long.parseLong(pid[1]), Short.parseShort(pid[0]), score));
            }
            rs.close();
            return true;
            }).get();
        } catch (Exception e){
            String s = "";
        }
        return candidates;
    }
}

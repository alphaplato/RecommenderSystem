package com.zcool.recoserver.service.feedserver;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.hologres.client.Get;
import com.alibaba.hologres.client.HoloClient;
import com.alibaba.hologres.client.Scan;
import com.alibaba.hologres.client.SortKeys;
import com.alibaba.hologres.client.exception.HoloClientException;
import com.alibaba.hologres.client.model.Record;
import com.alibaba.hologres.client.model.RecordScanner;
import com.alibaba.hologres.client.model.TableSchema;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.google.common.base.Stopwatch;
import com.plato.recoserver.recoserver.configure.CloudConfigConfiguration;
import com.plato.recoserver.recoserver.configure.RecoServerConfiguration;
import com.plato.recoserver.recoserver.core.ranker.feature.manager.UserFeatureManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author lishuguang
 * @date 2022/6/13
 **/

@SpringBootTest(properties = {
        "apollo.meta=http://apolloconfig-dev.in.zcool.cn/config",
        "hologres.client.profile.url=jdbc:postgresql://hgprecn-cn-2r42ppl1q001-cn-beijing.hologres.aliyuncs.com:80/profile_service",
        "hologres.client.profile.username=LTAI5tKwmYTF8EhrpgtTcdSt",
        "hologres.client.profile.password=bCNsmidzs9028bATP1CXZj5zn3PwI3",
        "hologres.client.profile.threadsize=60",
        "hologres.client.profile.batchsize=256",
        "hologres.client.profile.queuesize=128",
        "redis.client.host=r-2zem0286ielmbanc83pd.redis.rds.aliyuncs.com:6379",
        "redis.client.password=^!jmc7Y3B91kFdIN",
        "redis.client.db=0"
        })
@SpringJUnitConfig({RecoServerConfiguration.class, CloudConfigConfiguration.class, UserFeatureManager.class})
@DirtiesContext
@EnableConfigurationProperties
@RunWith(SpringRunner.class)
public class UserProfileBuilderTest {
    @ApolloJsonValue("${reco.server.profile.user_conf}")
    private JSONObject profileConf;
    @ApolloJsonValue("${reco.server.profile.rt_user_conf_scene}")
    private JSONObject rtProfileConf;
    @Value("${reco.server.feature.timeout:1}")
    private long timeOut;

    @Autowired
    private UserFeatureManager userFeatureManager;

    @Autowired
    private HoloClient client;

    private Record record;
    private final static String TABLE_NAME = "tableName";
    private final static String PRIMARY_KEY = "primaryKey";

    @Test
    public void testUserProfile() throws HoloClientException {
        System.out.println(timeOut);
        System.out.println(profileConf);
        long a = 1L;
        System.out.println(String.valueOf(a));
        try {
            TableSchema schema = client.getTableSchema(profileConf.getString(TABLE_NAME));
            Get get = Get.newBuilder(schema).setPrimaryKey(profileConf.getString(PRIMARY_KEY), "24482604").build();
            client.get(get).thenAcceptAsync(record ->this.record = record).get();
        } catch (HoloClientException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(record.getSchema().getColumnIndex("123"));
//        userFeatureManager.setUserProfile(record);
    }

   @Test
    public void testRtUserProfile() throws HoloClientException {
        String uids  = "2531950";
//        String uids = "23206690";
        String[] uidArr = uids.split(",");
        List<String> uidList = Arrays.asList(uidArr);
        Collections.shuffle(uidList);
        TableSchema schema = client.getTableSchema(rtProfileConf.getString(TABLE_NAME));
//        Scan.Builder scanBuilder = Scan.newBuilder(schema)
//                .withSelectedColumn("content_id")
//                .withSelectedColumn("content_type");
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis() / 1000;
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 24);
        long startTime = calendar.getTimeInMillis() / 1000;
//        System.out.println(endTime-startTime);
        String[] selectedColumns = JSONObject
                .parseArray(rtProfileConf.getJSONArray("selectedColumns").toJSONString(), String.class)
                .toArray(new  String[0]);
        List<Record> records = new LinkedList<>();
        for (String uid : uidList){
            Stopwatch sw = Stopwatch.createStarted();
//            System.out.println(uid);
            Scan scan = Scan.newBuilder(schema)
                    .withSelectedColumn("uid")
                    .withSelectedColumns(selectedColumns)
                    .addEqualFilter("uid", uid)
                    .addRangeFilter("time",startTime, endTime)
                    .setFetchSize(10000)
                    .setSortKeys(SortKeys.CLUSTERING_KEY)
                    .build();
            RecordScanner rs = client.scan(scan);
            while (rs.next()) {
                Record record = rs.getRecord();
                records.add(record);
            }

        }
        Collections.reverse(records);
//        userFeatureManager.setRtUserProfile(records);
    }

    @Test
    public void testManager() throws HoloClientException {
        testUserProfile();
        testRtUserProfile();
//        System.out.println(userFeatureManager.getFeature());
    }
}

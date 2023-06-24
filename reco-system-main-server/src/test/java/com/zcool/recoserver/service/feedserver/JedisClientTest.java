package com.zcool.recoserver.service.feedserver;

import com.plato.recoserver.recoserver.configure.RecoServerConfiguration;
import com.plato.recoserver.recoserver.datamanager.dao.redis.JedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.PipelineBase;
import redis.clients.jedis.Response;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Kevin
 * @date 2022-03-03
 */
@SpringBootTest(properties = {
        "hologres.client.profile.url=jdbc:postgresql://hgprecn-cn-2r42ppl1q001-cn-beijing.hologres.aliyuncs.com:80/profile_service",
        "hologres.client.profile.username=LTAI5tKwmYTF8EhrpgtTcdSt",
        "hologres.client.profile.password=bCNsmidzs9028bATP1CXZj5zn3PwI3",
        "hologres.client.profile.threadsize=2",
        "hologres.client.profile.batchsize=256",
        "hologres.client.profile.queuesize=128",
        "redis.client.host=r-2zem0286ielmbanc83pd.redis.rds.aliyuncs.com:6379",
        "redis.client.password=^!jmc7Y3B91kFdIN",
        "redis.client.db=0"
})
@SpringJUnitConfig(RecoServerConfiguration.class)
@DirtiesContext
@EnableConfigurationProperties
@RunWith(SpringRunner.class)
public class JedisClientTest {

    @Autowired
    private JedisClient jedisClient;

    @Test
    public void executeTest() {
        final byte[] keyBytes = "default_i_1_14366356".getBytes(StandardCharsets.UTF_8);
        byte[] valueBytes = jedisClient.execute(jedis -> jedis.get(keyBytes));
        valueBytes = Base64.getDecoder().decode(valueBytes);
        System.out.println(valueBytes.length);
        List<byte[]> bytes = new LinkedList<>();
        bytes.add("default_i_1_142366356".getBytes(StandardCharsets.UTF_8));
        List<Response<byte[]>> strings2 = jedisClient.executeRPipeline(bytes, PipelineBase::get);
        strings2.stream().forEach(e ->
        {
            byte[] t = e.get();
            byte[] d = Base64.getDecoder().decode(t);
            System.out.println(d.length);
        });
//        int count = valueBytes.length / 18;
//        for (int i = 0; i < count; i++) {
//            int start = i * 18;
//            int type = RecUtils.bytesToShort(valueBytes, start);
//            long id = RecUtils.bytesToLong(valueBytes, start + 2);
//            double score = RecUtils.bytesToDouble(valueBytes, start + 10);
//            System.out.println(type + "," + id + "," + score);
//        }
    }

    @Test
    public void tairBloomTest() {
//        Boolean added = jedisClient.executeBloom(tairBloom -> tairBloom.bfadd("test-bloom", "bloom-value1"));
//        Assert.assertTrue("bloom-value1 should not be added successfully", added);
//        jedisClient.executeBloom(tairBloom -> tairBloom.bfreserve("test-bloom", 1000000, 0.0001));
//        String[] prepare = new String[100000];
//        for (int i = 0; i < 100000; i++) {
//            prepare[i] = UUID.randomUUID().toString();
//        }
//        jedisClient.executeBloom(tairBloom -> tairBloom.bfmadd("test-bloom", prepare));
//        List<String> values = new LinkedList<>();
//        for (int i = 0; i < 10000; i++) {
//            values.add(UUID.randomUUID().toString());
//        }
//        String[] arrays = values.toArray(new String[values.size()]);
//        jedisClient.executeBloom(tairBloom -> tairBloom.bfmadd("test-bloom", arrays));
//        long st = System.currentTimeMillis();
//        Boolean[] result = null;
//        for (int i = 0; i < 50; i++) {
//            result = jedisClient.executeBloom(tairBloom -> tairBloom.bfmexists("test-bloom", arrays));
//        }
//        System.out.println(System.currentTimeMillis() - st);
//        System.out.println(result.length);
//        Assert.assertNotNull(result);
//        for (boolean b : result) {
//            Assert.assertTrue(b);
//        }
//        jedisClient.execute(jedis -> jedis.del("test-bloom"));
//        boolean b = jedisClient.execute(jedis -> jedis.exists("test-bloom"));
//        System.out.println(b);
    }

//    @Test
//    public void testUserProfile() throws InvalidProtocolBufferException {
////        String str = jedisClient.execute(jedis -> jedis.get("19720883"));
////        byte[] bytes = Base64.getDecoder().decode(str);
//        byte[] bytes = jedisClient.execute(jedis -> jedis.get("u_1001859".getBytes(StandardCharsets.UTF_8)));
//
//        System.out.println(bytes.length);
//        System.out.println(UserProfile.parseFrom(Base64.getDecoder().decode(bytes)));
////        jedisClient.execute(jedis -> jedis.set("u_1001859".getBytes(StandardCharsets.UTF_8), bytes));
//        System.out.println("===============================");
//    }

//    @Test
//    public void testRProfile() throws InvalidProtocolBufferException {
//        Set<byte[]> bytes = jedisClient.execute(jedis -> jedis.smembers("rt_u_0".getBytes(StandardCharsets.UTF_8)));
//
//        System.out.println(bytes.size());
//        for (byte[] b : bytes) {
//            System.out.println(RTUserBehavior.parseFrom(Base64.getDecoder().decode(b)));
//        }
//        System.out.println("===============================");
//    }

    @Test
    public void testScan() {
        jedisClient.execute(jedis -> {
            String cursor = "0";
            ScanParams params = new ScanParams();
            params.match("defaul_i_1111*").count(1);
            ScanResult<String> result = jedis.scan(cursor, params);
            System.out.println(result.getCursor() + "====" + result.getResult().size());
            if (!result.getResult().isEmpty()) {
                long count = jedis.del(result.getResult().toArray(new String[]{}));
                System.out.println("delete count==" + count);
            }
            cursor = result.getCursor();
            return null;
        });
    }
}

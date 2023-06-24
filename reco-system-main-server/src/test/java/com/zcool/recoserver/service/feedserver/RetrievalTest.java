package com.zcool.recoserver.service.feedserver;

import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.configure.RecoServerConfiguration;
import com.plato.recoserver.recoserver.core.retrieval.RedisRetrievalItemProvider;
import com.plato.recoserver.recoserver.core.retrieval.inf.IRetrievalDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Kevin
 * @date 2022-03-22
 */
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
}, classes = {RedisRetrievalItemProvider.class})
@SpringJUnitConfig(RecoServerConfiguration.class)
@DirtiesContext
@EnableConfigurationProperties
@RunWith(SpringRunner.class)
public class RetrievalTest {

    @Autowired
    private IRetrievalDataProvider<CandidateItem> redisRetrievalItemProvider;

    @Test
    public void testCommonRetrieval() {
        List<Item> items = new LinkedList<>();
        items.add(new CandidateItem(2110011L, 4, 0.1));
        items.add(new CandidateItem(21131L, 4, 0.2));
        items.add(new CandidateItem(21511L, 4, 0.3));
        items.forEach(e-> {
            CandidateItem r = (CandidateItem) e;
            r.setRankScore(0.01);
//            System.out.println(e);
        });
    }
}

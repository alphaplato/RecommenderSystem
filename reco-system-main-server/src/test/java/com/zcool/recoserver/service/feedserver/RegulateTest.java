package com.zcool.recoserver.service.feedserver;

import com.zcool.platform.spread.regulate.grpc.lib.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
@SpringBootTest(properties = {
//        "grpc.client.reco-server-rpc.address=static://recommend-test.in.zcool.cn:443",
//        "grpc.client.reco-server-rpc.negotiation-type=tls",
        "grpc.client.spread-regulator.negotiation-type=plaintext",
        "grpc.client.spread-regulator.address=static://127.0.0.1:9090",
        "spring.application.name=Junit Test",
        "grpc.client.spread-regulator.enable-keep-alive=true",
})
@SpringJUnitConfig(RecoRpcTestConfiguration.class)
@RunWith(SpringRunner.class)
public class RegulateTest {
    @GrpcClient("spread-regulator")
    SpreadRegulatorServiceGrpc.SpreadRegulatorServiceBlockingStub regulatorStub;

    @Test
    public void testRegulate() {
        List<Item> itemList = new LinkedList<>();
        long[] productIds = new long[]{
                11872960,
                11872905,
                11872900,
                11872898,
                11872896,
                11872895,
                11872894,
                11872893,
                11872889,
                11872887
        };

//
//        for (long productId : productIds) {
//            itemList.add(Item.newBuilder().setId(productId).setType(ItemType.PRODUCT).build());
//        }
//
//        itemList.add(Item.newBuilder().setId(1).setType(ItemType.PRODUCT).build());
//        itemList.add(Item.newBuilder().setId(10).setType(ItemType.PRODUCT).build());
//        itemList.add(Item.newBuilder().setId(100).setType(ItemType.PRODUCT).build());
//        itemList.add(Item.newBuilder().setId(101).setType(ItemType.ARTICLE).build());
//        itemList.add(Item.newBuilder().setId(102).setType(ItemType.ARTICLE).build());
//        itemList.add(Item.newBuilder().setId(103).setType(ItemType.FAVORITE_FOLDER).build());
//        itemList.add(Item.newBuilder().setId(104).setType(ItemType.FAVORITE_FOLDER).build());
//        itemList.add(Item.newBuilder().setId(1005).setType(ItemType.FAVORITE_FOLDER_IMAGE).build());
        RegulateRequest regulateRequest = RegulateRequest.newBuilder()
                .addAllItems(itemList)
                .setSceneCode("community_home_feed")
                .setLoginId(1)
                .build();
        System.out.println("regulateRequest" + regulateRequest);
        long startAt = System.currentTimeMillis();
        RegulateResponse regulate = regulatorStub.regulate(regulateRequest);
        System.out.println("regulateCost:" + (System.currentTimeMillis() - startAt));
        System.out.println("regulateResults:" + regulate.getRegulateResultList());
    }
}

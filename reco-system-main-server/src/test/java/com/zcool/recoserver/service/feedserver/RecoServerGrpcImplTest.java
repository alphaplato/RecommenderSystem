package com.zcool.recoserver.service.feedserver;

import com.plato.recoserver.grpc.service.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Kevin
 * @date 2022-03-03
 */
@SpringBootTest(properties = {
//        "grpc.client.reco-server-rpc.address=static://recommend-test.in.zcool.cn:443",
//        "grpc.client.reco-server-rpc.negotiation-type=tls",
        "grpc.client.reco-server-rpc.negotiation-type=plaintext",
        "grpc.client.reco-server-rpc.address=static://127.0.0.1:7071",
        "spring.application.name=Junit Test",
        "grpc.client.reco-server-rpc.enable-keep-alive=true",
})
@SpringJUnitConfig(RecoRpcTestConfiguration.class)
@DirtiesContext
@RunWith(SpringRunner.class)
public class RecoServerGrpcImplTest {

    @GrpcClient("reco-server-rpc")
    private RecoServerServiceGrpc.RecoServerServiceBlockingStub recoServerServiceBlockingStub;

    @Test
    @DirtiesContext
    public void grpcTest() {
        RecoRequest request = RecoRequest.newBuilder()
                .setRequestId(String.valueOf(System.currentTimeMillis()))
                .setDeviceId("zzzz")
                .setUserId(1001859L)
                .setNum(30)
                .setPageNum(0)
                .setPlatform(Platform.PLATFORM_ANDROID)
                .setRecScene("home_rec")
                .build();
        RecoResponse response = recoServerServiceBlockingStub.getRecoResult(request);
        System.out.println(response);
    }
}

package com.zcool.recoserver.service.feedserver;

import com.plato.recoserver.recoserver.grpc.GrpcAuthInterceptor;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin
 * @date 2022-03-03
 */
@Configuration
@ImportAutoConfiguration({
        GrpcClientAutoConfiguration.class
})
public class RecoRpcTestConfiguration {

    @Bean
    public GrpcAuthInterceptor grpcAuthInterceptor() {
        return new GrpcAuthInterceptor();
    }
}

package com.plato.recoserver.recoserver.grpc;

import io.grpc.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Kevin
 * @date 2022-03-21
 */
@Component
public class GrpcAuthInterceptor implements ClientInterceptor {
    @Value("${reco.rank.ali-pai.auth.token:OTdlNzEwMGJkYjlhMjY4ZjhhNzNlZjZhMGU1YWQyMWYyODQwOTg1ZA==}")
    private String token;
    private static final String HEADER_KEY_AUTH = "Authorization";

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                if (StringUtils.isNotBlank(token)) {
                    Metadata.Key<String> key = Metadata.Key.of(HEADER_KEY_AUTH,  Metadata.ASCII_STRING_MARSHALLER);
                    headers.put(key, token);
                }
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        super.onHeaders(headers);
                    }
                }, headers);
            }
        };
    }
}

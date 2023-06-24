package com.plato.recoserver.grpc.service;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.42.2)",
    comments = "Source: reco_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RecoServerServiceGrpc {

  private RecoServerServiceGrpc() {}

  public static final String SERVICE_NAME = "com.plato.recoserver.grpc.service.RecoServerService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.plato.recoserver.grpc.service.RecoRequest,
      com.plato.recoserver.grpc.service.RecoResponse> getGetRecoResultMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getRecoResult",
      requestType = com.plato.recoserver.grpc.service.RecoRequest.class,
      responseType = com.plato.recoserver.grpc.service.RecoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.plato.recoserver.grpc.service.RecoRequest,
      com.plato.recoserver.grpc.service.RecoResponse> getGetRecoResultMethod() {
    io.grpc.MethodDescriptor<com.plato.recoserver.grpc.service.RecoRequest, com.plato.recoserver.grpc.service.RecoResponse> getGetRecoResultMethod;
    if ((getGetRecoResultMethod = RecoServerServiceGrpc.getGetRecoResultMethod) == null) {
      synchronized (RecoServerServiceGrpc.class) {
        if ((getGetRecoResultMethod = RecoServerServiceGrpc.getGetRecoResultMethod) == null) {
          RecoServerServiceGrpc.getGetRecoResultMethod = getGetRecoResultMethod =
              io.grpc.MethodDescriptor.<com.plato.recoserver.grpc.service.RecoRequest, com.plato.recoserver.grpc.service.RecoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getRecoResult"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.plato.recoserver.grpc.service.RecoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.plato.recoserver.grpc.service.RecoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RecoServerServiceMethodDescriptorSupplier("getRecoResult"))
              .build();
        }
      }
    }
    return getGetRecoResultMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RecoServerServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RecoServerServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RecoServerServiceStub>() {
        @java.lang.Override
        public RecoServerServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RecoServerServiceStub(channel, callOptions);
        }
      };
    return RecoServerServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RecoServerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RecoServerServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RecoServerServiceBlockingStub>() {
        @java.lang.Override
        public RecoServerServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RecoServerServiceBlockingStub(channel, callOptions);
        }
      };
    return RecoServerServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RecoServerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RecoServerServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RecoServerServiceFutureStub>() {
        @java.lang.Override
        public RecoServerServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RecoServerServiceFutureStub(channel, callOptions);
        }
      };
    return RecoServerServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class RecoServerServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getRecoResult(com.plato.recoserver.grpc.service.RecoRequest request,
        io.grpc.stub.StreamObserver<com.plato.recoserver.grpc.service.RecoResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRecoResultMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetRecoResultMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.plato.recoserver.grpc.service.RecoRequest,
                com.plato.recoserver.grpc.service.RecoResponse>(
                  this, METHODID_GET_RECO_RESULT)))
          .build();
    }
  }

  /**
   */
  public static final class RecoServerServiceStub extends io.grpc.stub.AbstractAsyncStub<RecoServerServiceStub> {
    private RecoServerServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RecoServerServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RecoServerServiceStub(channel, callOptions);
    }

    /**
     */
    public void getRecoResult(com.plato.recoserver.grpc.service.RecoRequest request,
        io.grpc.stub.StreamObserver<com.plato.recoserver.grpc.service.RecoResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRecoResultMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RecoServerServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<RecoServerServiceBlockingStub> {
    private RecoServerServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RecoServerServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RecoServerServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.plato.recoserver.grpc.service.RecoResponse getRecoResult(com.plato.recoserver.grpc.service.RecoRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRecoResultMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RecoServerServiceFutureStub extends io.grpc.stub.AbstractFutureStub<RecoServerServiceFutureStub> {
    private RecoServerServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RecoServerServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RecoServerServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.plato.recoserver.grpc.service.RecoResponse> getRecoResult(
        com.plato.recoserver.grpc.service.RecoRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRecoResultMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_RECO_RESULT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RecoServerServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RecoServerServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_RECO_RESULT:
          serviceImpl.getRecoResult((com.plato.recoserver.grpc.service.RecoRequest) request,
              (io.grpc.stub.StreamObserver<com.plato.recoserver.grpc.service.RecoResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RecoServerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RecoServerServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.plato.recoserver.grpc.service.RecoServer.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RecoServerService");
    }
  }

  private static final class RecoServerServiceFileDescriptorSupplier
      extends RecoServerServiceBaseDescriptorSupplier {
    RecoServerServiceFileDescriptorSupplier() {}
  }

  private static final class RecoServerServiceMethodDescriptorSupplier
      extends RecoServerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RecoServerServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RecoServerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RecoServerServiceFileDescriptorSupplier())
              .addMethod(getGetRecoResultMethod())
              .build();
        }
      }
    }
    return result;
  }
}

package network.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * 1. A definição do Serviço (O "Controlador" da Rede)
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: fanorona.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FanoronaServiceGrpc {

  private FanoronaServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "FanoronaService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<network.grpc.PacoteRede,
      network.grpc.PacoteRede> getJogarMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Jogar",
      requestType = network.grpc.PacoteRede.class,
      responseType = network.grpc.PacoteRede.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<network.grpc.PacoteRede,
      network.grpc.PacoteRede> getJogarMethod() {
    io.grpc.MethodDescriptor<network.grpc.PacoteRede, network.grpc.PacoteRede> getJogarMethod;
    if ((getJogarMethod = FanoronaServiceGrpc.getJogarMethod) == null) {
      synchronized (FanoronaServiceGrpc.class) {
        if ((getJogarMethod = FanoronaServiceGrpc.getJogarMethod) == null) {
          FanoronaServiceGrpc.getJogarMethod = getJogarMethod =
              io.grpc.MethodDescriptor.<network.grpc.PacoteRede, network.grpc.PacoteRede>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Jogar"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  network.grpc.PacoteRede.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  network.grpc.PacoteRede.getDefaultInstance()))
              .setSchemaDescriptor(new FanoronaServiceMethodDescriptorSupplier("Jogar"))
              .build();
        }
      }
    }
    return getJogarMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FanoronaServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FanoronaServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FanoronaServiceStub>() {
        @java.lang.Override
        public FanoronaServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FanoronaServiceStub(channel, callOptions);
        }
      };
    return FanoronaServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FanoronaServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FanoronaServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FanoronaServiceBlockingStub>() {
        @java.lang.Override
        public FanoronaServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FanoronaServiceBlockingStub(channel, callOptions);
        }
      };
    return FanoronaServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FanoronaServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FanoronaServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FanoronaServiceFutureStub>() {
        @java.lang.Override
        public FanoronaServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FanoronaServiceFutureStub(channel, callOptions);
        }
      };
    return FanoronaServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * 1. A definição do Serviço (O "Controlador" da Rede)
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Streaming Bidirecional: Tanto o cliente quanto o servidor recebem e enviam um fluxo contínuo
     * </pre>
     */
    default io.grpc.stub.StreamObserver<network.grpc.PacoteRede> jogar(
        io.grpc.stub.StreamObserver<network.grpc.PacoteRede> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getJogarMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service FanoronaService.
   * <pre>
   * 1. A definição do Serviço (O "Controlador" da Rede)
   * </pre>
   */
  public static abstract class FanoronaServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FanoronaServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service FanoronaService.
   * <pre>
   * 1. A definição do Serviço (O "Controlador" da Rede)
   * </pre>
   */
  public static final class FanoronaServiceStub
      extends io.grpc.stub.AbstractAsyncStub<FanoronaServiceStub> {
    private FanoronaServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FanoronaServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FanoronaServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Streaming Bidirecional: Tanto o cliente quanto o servidor recebem e enviam um fluxo contínuo
     * </pre>
     */
    public io.grpc.stub.StreamObserver<network.grpc.PacoteRede> jogar(
        io.grpc.stub.StreamObserver<network.grpc.PacoteRede> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getJogarMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service FanoronaService.
   * <pre>
   * 1. A definição do Serviço (O "Controlador" da Rede)
   * </pre>
   */
  public static final class FanoronaServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FanoronaServiceBlockingStub> {
    private FanoronaServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FanoronaServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FanoronaServiceBlockingStub(channel, callOptions);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service FanoronaService.
   * <pre>
   * 1. A definição do Serviço (O "Controlador" da Rede)
   * </pre>
   */
  public static final class FanoronaServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<FanoronaServiceFutureStub> {
    private FanoronaServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FanoronaServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FanoronaServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_JOGAR = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_JOGAR:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.jogar(
              (io.grpc.stub.StreamObserver<network.grpc.PacoteRede>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getJogarMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              network.grpc.PacoteRede,
              network.grpc.PacoteRede>(
                service, METHODID_JOGAR)))
        .build();
  }

  private static abstract class FanoronaServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FanoronaServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return network.grpc.FanoronaProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("FanoronaService");
    }
  }

  private static final class FanoronaServiceFileDescriptorSupplier
      extends FanoronaServiceBaseDescriptorSupplier {
    FanoronaServiceFileDescriptorSupplier() {}
  }

  private static final class FanoronaServiceMethodDescriptorSupplier
      extends FanoronaServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FanoronaServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (FanoronaServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FanoronaServiceFileDescriptorSupplier())
              .addMethod(getJogarMethod())
              .build();
        }
      }
    }
    return result;
  }
}

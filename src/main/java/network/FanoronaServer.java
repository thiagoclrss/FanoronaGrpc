package network;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import network.grpc.FanoronaServiceGrpc;
import network.grpc.PacoteRede;
import java.io.IOException;
import java.util.function.Consumer;

public class FanoronaServer {
    private Server grpcServer;
    private final int porta;
    // O callback agora trafega o objeto binário seguro do gRPC, e não mais uma String crua
    private final Consumer<PacoteRede> aoReceberMensagem;

    // Este é o "tubo" por onde enviaremos dados para o cliente
    private StreamObserver<PacoteRede> clienteObserver;

    public FanoronaServer(int porta, Consumer<PacoteRede> aoReceberMensagem) {
        this.porta = porta;
        this.aoReceberMensagem = aoReceberMensagem;
    }

    public void iniciar() throws IOException {
        grpcServer = ServerBuilder.forPort(porta)
                .addService(new FanoronaServiceImpl())
                .build()
                .start();
        System.out.println("Servidor gRPC iniciado na porta " + porta);
    }

    public void enviarMensagem(PacoteRede pacote) {
        if (clienteObserver != null) {
            clienteObserver.onNext(pacote); // Empurra o pacote pelo tubo
        }
    }

    public void parar() {
        if (grpcServer != null) {
            grpcServer.shutdown();
        }
    }

    /**
     * Esta classe interna implementa o contrato que definimos no arquivo .proto.
     */
    private class FanoronaServiceImpl extends FanoronaServiceGrpc.FanoronaServiceImplBase {

        @Override
        public StreamObserver<PacoteRede> jogar(StreamObserver<PacoteRede> responseObserver) {
            // 1. Guardamos a referência do cliente para poder enviar mensagens a ele depois
            clienteObserver = responseObserver;

            // 2. Retornamos um novo observador que dita o que fazer quando o cliente NOS enviar algo
            return new StreamObserver<PacoteRede>() {
                @Override
                public void onNext(PacoteRede pacoteRede) {
                    // Assim que um pacote chega, acionamos o Controlador (ViewModel)
                    if (aoReceberMensagem != null) {
                        aoReceberMensagem.accept(pacoteRede);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Conexão com o cliente perdida: " + throwable.getMessage());
                    clienteObserver = null;
                }

                @Override
                public void onCompleted() {
                    System.out.println("O cliente encerrou a conexão de forma amigável.");
                    clienteObserver = null;
                }
            };
        }
    }
}
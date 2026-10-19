package network;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import network.grpc.FanoronaServiceGrpc;
import network.grpc.PacoteRede;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FanoronaServer {
    private Server grpcServer;
    private final int porta;
    private final Consumer<PacoteRede> aoReceberMensagem;
    private volatile StreamObserver<PacoteRede> clienteObserver;
    private final Object lockEnvio = new Object();
    private final Runnable aoConectar;
    private final Logger log = Logger.getLogger(FanoronaServer.class.getName());

    public FanoronaServer(int porta, Consumer<PacoteRede> aoReceberMensagem, Runnable aoConectar) {
        this.porta = porta;
        this.aoReceberMensagem = aoReceberMensagem;
        this.aoConectar = aoConectar;
    }

    public void iniciar() throws IOException {
        grpcServer = ServerBuilder.forPort(porta)
                .addService(new FanoronaServiceImpl())
                .build()
                .start();
        log.info("Servidor gRPC iniciado na porta " + porta);
    }

    public void enviarMensagem(PacoteRede pacote) {
        StreamObserver<PacoteRede> obs = clienteObserver;
        if (obs != null) {
            synchronized (lockEnvio) {
                obs.onNext(pacote);
            }
        } else {
            log.info("Nenhum cliente conectado, pacote descartado");
        }
    }

    public void parar() {
        if (grpcServer != null) {
            grpcServer.shutdown();
        }
    }

    private class FanoronaServiceImpl extends FanoronaServiceGrpc.FanoronaServiceImplBase {

        @Override
        public StreamObserver<PacoteRede> jogar(StreamObserver<PacoteRede> responseObserver) {
            clienteObserver = responseObserver;
            if (aoConectar != null) aoConectar.run();

            return new StreamObserver<PacoteRede>() {
                @Override
                public void onNext(PacoteRede pacoteRede) {
                    if (aoReceberMensagem != null) {
                        aoReceberMensagem.accept(pacoteRede);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    log.log(Level.SEVERE, "Conexão com o cliente perdida: " + throwable.getMessage(), throwable);
                    clienteObserver = null;
                }

                @Override
                public void onCompleted() {
                    log.info("O cliente encerrou a conexão de forma amigável.");
                    clienteObserver = null;
                }
            };
        }
    }
}
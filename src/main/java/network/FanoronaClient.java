package network;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import network.grpc.FanoronaServiceGrpc;
import network.grpc.FanoronaServiceGrpc.FanoronaServiceStub;
import network.grpc.PacoteRede;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FanoronaClient {
    private ManagedChannel canal;
    private FanoronaServiceStub stubAssincrono;
    private StreamObserver<PacoteRede> fluxoDeEnvio;
    private final Object lockEnvio = new Object();
    private final String host;
    private final int porta;
    private final Consumer<PacoteRede> aoReceberMensagem;
    private final Logger log =  Logger.getLogger(FanoronaClient.class.getName());

    public FanoronaClient(String host, int porta, Consumer<PacoteRede> aoReceberMensagem) {
        this.host = host;
        this.porta = porta;
        this.aoReceberMensagem = aoReceberMensagem;
    }

    public void iniciar() {
        canal = ManagedChannelBuilder.forAddress(host, porta)
                .usePlaintext()
                .build();
        canal.getState(true);

        stubAssincrono = FanoronaServiceGrpc.newStub(canal);

        StreamObserver<PacoteRede> fluxoDeRecebimento = new StreamObserver<PacoteRede>() {
            @Override
            public void onNext(PacoteRede pacoteRede) {
                if (aoReceberMensagem != null) {
                    aoReceberMensagem.accept(pacoteRede);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.log(Level.SEVERE, throwable.getMessage(), throwable);
            }

            @Override
            public void onCompleted() {
                log.info("O servidor encerrou a partida.");
            }
        };

        fluxoDeEnvio = stubAssincrono.jogar(fluxoDeRecebimento);
        log.log(Level.INFO, "Cliente gRPC conectado ao servidor " + host + ":" + porta);
    }

    public void enviarMensagem(PacoteRede pacote) {
        if (fluxoDeEnvio != null) {
            synchronized (lockEnvio) {
                fluxoDeEnvio.onNext(pacote);
            }
        }
    }

    public void parar() {
        if (fluxoDeEnvio != null) {
            fluxoDeEnvio.onCompleted();
        }
        if (canal != null && !canal.isShutdown()) {
            canal.shutdown();
        }
    }
}
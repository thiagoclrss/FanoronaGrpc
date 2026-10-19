package network;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import network.grpc.FanoronaServiceGrpc;
import network.grpc.PacoteRede;

import java.util.function.Consumer;

public class FanoronaClient {
    private ManagedChannel canal;
    private FanoronaServiceGrpc.FanoronaServiceStub stubAssincrono;
    private StreamObserver<PacoteRede> fluxoDeEnvio; // Tubo para mandar dados ao servidor

    private final String host;
    private final int porta;
    private final Consumer<PacoteRede> aoReceberMensagem;

    public FanoronaClient(String host, int porta, Consumer<PacoteRede> aoReceberMensagem) {
        this.host = host;
        this.porta = porta;
        this.aoReceberMensagem = aoReceberMensagem;
    }

    public void iniciar() {
        // 1. Criamos o canal de comunicação HTTP/2 apontando para o IP do servidor
        canal = ManagedChannelBuilder.forAddress(host, porta)
                .usePlaintext() // Essencial para testes sem certificados de segurança (SSL/TLS)
                .build();

        // 2. Criamos o "Stub" assíncrono. Ele é a ponte mágica do gRPC!
        stubAssincrono = FanoronaServiceGrpc.newStub(canal);

        // 3. Criamos o nosso observador (O que fazemos quando o SERVIDOR nos mandar algo?)
        StreamObserver<PacoteRede> fluxoDeRecebimento = new StreamObserver<PacoteRede>() {
            @Override
            public void onNext(PacoteRede pacoteRede) {
                // Quando o pacote chega, repassamos para o Controlador do JavaFX
                if (aoReceberMensagem != null) {
                    aoReceberMensagem.accept(pacoteRede);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Erro na comunicação com o servidor: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("O servidor encerrou a partida.");
            }
        };

        // 4. Invocamos o método remoto 'jogar'.
        // Passamos o nosso observador de recebimento e ele nos devolve o tubo de envio.
        fluxoDeEnvio = stubAssincrono.jogar(fluxoDeRecebimento);

        System.out.println("Cliente gRPC conectado ao servidor " + host + ":" + porta);
    }

    public void enviarMensagem(PacoteRede pacote) {
        if (fluxoDeEnvio != null) {
            fluxoDeEnvio.onNext(pacote); // Despacha o pacote binário pela rede
        }
    }

    public void parar() {
        if (fluxoDeEnvio != null) {
            fluxoDeEnvio.onCompleted(); // Fecha o tubo de envio educadamente
        }
        if (canal != null && !canal.isShutdown()) {
            canal.shutdown(); // Desliga a conexão de rede
        }
    }
}
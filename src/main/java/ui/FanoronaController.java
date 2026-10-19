package ui;

import core.FanoronaCore;
import core.Peca;
import core.Posicao;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import network.FanoronaClient;
import network.FanoronaServer;
// Importações geradas pelo Protobuf/gRPC
import network.grpc.AcaoSimples;
import network.grpc.MensagemChat;
import network.grpc.Movimento;
import network.grpc.PacoteRede;
import network.grpc.TipoAcao;

import java.io.IOException;

public class FanoronaController {

    @FXML private GridPane tabuleiroGrid;
    @FXML private TextArea chatArea;
    @FXML private TextField inputChat;
    @FXML private Label statusTurno;

    private FanoronaCore jogo;
    private FanoronaServer servidor = null;
    private FanoronaClient cliente = null;
    private Posicao pecaSelecionada = null;
    private boolean jogoFinalizado = false;
    private Peca minhaCor;

    @FXML
    public void initialize() {
        jogo = new FanoronaCore();
        desenharTabuleiroNaTela();
    }

    public void iniciarConexao(boolean isServidor, String ipServidor) {
        if (isServidor) {
            minhaCor = Peca.BRANCA;
            statusTurno.setText("Aguardando adversário conectar...");
            servidor = new FanoronaServer(5000,
                    pacote -> Platform.runLater(() -> processarMensagemDaRede(pacote)),
                    () -> Platform.runLater(() -> {
                        chatArea.appendText("[SISTEMA] Adversário conectado.\n");
                        atualizarStatusTurno();
                    }));
            try {
                servidor.iniciar();
            } catch (IOException e) {
                statusTurno.setText("Erro ao iniciar servidor: " + e.getMessage());
                return;
            }
        } else {
            minhaCor = Peca.PRETA;
            statusTurno.setText("Conectando a " + ipServidor + "...");
            cliente = new FanoronaClient(ipServidor, 5000,
                    pacote -> Platform.runLater(() -> processarMensagemDaRede(pacote)));
            cliente.iniciar();
            atualizarStatusTurno();
        }
    }

    // O envio agora exige um PacoteRede validado, impossibilitando strings maliciosas
    private void enviarPacoteRede(PacoteRede pacote) {
        if (servidor != null) servidor.enviarMensagem(pacote);
        if (cliente != null) cliente.enviarMensagem(pacote);
    }

    // O Roteador de mensagens agora usa os métodos has...() gerados pelo Protobuf
    private void processarMensagemDaRede(PacoteRede pacote) {
        if (pacote.hasMovimento()) {
            Movimento mov = pacote.getMovimento();
            Posicao origem = new Posicao(mov.getLinhaOrigem(), mov.getColunaOrigem());
            Posicao destino = new Posicao(mov.getLinhaDestino(), mov.getColunaDestino());

            jogo.tentarMovimento(origem, destino);
            atualizarTabuleiroNaTela();

            Peca vencedor = jogo.verificarVencedor();
            if (vencedor != null) {
                anunciarVencedor(vencedor);
            }
            atualizarStatusTurno();

        } else if (pacote.hasChat()) {
            chatArea.appendText("Adversário: " + pacote.getChat().getTexto() + "\n");

        } else if (pacote.hasAcao()) {
            AcaoSimples acao = pacote.getAcao();
            if (acao.getTipo() == TipoAcao.PASSAR_TURNO) {
                jogo.finalizarTurno();
                atualizarTabuleiroNaTela();
                atualizarStatusTurno();
            } else if (acao.getTipo() == TipoAcao.DESISTIR) {
                anunciarDesistencia(minhaCor, false);
            }
        }
    }

    @FXML
    public void enviarChat() {
        String texto = inputChat.getText();
        if (!texto.isBlank()) {
            chatArea.appendText("Você: " + texto + "\n");

            // Usando o Builder do gRPC para construir a mensagem
            PacoteRede pacote = PacoteRede.newBuilder()
                    .setChat(MensagemChat.newBuilder().setTexto(texto).build())
                    .build();

            enviarPacoteRede(pacote);
            inputChat.clear();
        }
    }

    @FXML
    public void desistirPartida() {
        if (jogoFinalizado) return;

        PacoteRede pacote = PacoteRede.newBuilder()
                .setAcao(AcaoSimples.newBuilder().setTipo(TipoAcao.DESISTIR).build())
                .build();
        enviarPacoteRede(pacote);

        Peca vencedor = (minhaCor == Peca.BRANCA) ? Peca.PRETA : Peca.BRANCA;
        anunciarDesistencia(vencedor, true);
    }

    @FXML
    public void passarTurno() {
        if (jogoFinalizado) return;
        if (jogo.getTurnoAtual() != minhaCor) {
            System.out.println("Ação bloqueada: Não é o seu turno.");
            return;
        }

        jogo.finalizarTurno();
        pecaSelecionada = null;
        atualizarTabuleiroNaTela();

        PacoteRede pacote = PacoteRede.newBuilder()
                .setAcao(AcaoSimples.newBuilder().setTipo(TipoAcao.PASSAR_TURNO).build())
                .build();
        enviarPacoteRede(pacote);

        atualizarStatusTurno();
    }

    private void atualizarStatusTurno() {
        if (jogoFinalizado) {
            statusTurno.setText("Partida Encerrada");
            statusTurno.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #7f8c8d;");
            return;
        }

        if (jogo.getTurnoAtual() == minhaCor) {
            statusTurno.setText("É a sua vez!");
            statusTurno.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
        } else {
            statusTurno.setText("Turno do adversário...");
            statusTurno.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
        }
    }

    private void desenharTabuleiroNaTela() {
        atualizarTabuleiroNaTela();
    }

    private void atualizarTabuleiroNaTela() {
        tabuleiroGrid.getChildren().clear();

        for (int linha = 0; linha < 5; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                StackPane casa = new StackPane();
                casa.setPrefSize(80, 80);

                Line linhaH = new Line(0, 0, 80, 0);
                linhaH.setStrokeWidth(2);

                Line linhaV = new Line(0, 0, 0, 80);
                linhaV.setStrokeWidth(2);

                casa.getChildren().addAll(linhaH, linhaV);

                if ((linha + coluna) % 2 == 0) {
                    Line diag1 = new Line(0, 0, 80, 80);
                    diag1.setStrokeWidth(2);

                    Line diag2 = new Line(80, 0, 0, 80);
                    diag2.setStrokeWidth(2);

                    casa.getChildren().addAll(diag1, diag2);
                }

                Peca pecaCore = jogo.getPeca(linha, coluna);
                if (pecaCore != Peca.VAZIA) {
                    Circle pecaVisual = new Circle(28);
                    pecaVisual.setFill(pecaCore == Peca.BRANCA ? Color.WHITE : Color.BLACK);
                    pecaVisual.setStroke(Color.DARKGRAY);
                    pecaVisual.setStrokeWidth(2);

                    if (pecaSelecionada != null && pecaSelecionada.linha() == linha && pecaSelecionada.coluna() == coluna) {
                        DropShadow brilho = new DropShadow();
                        brilho.setColor(Color.CYAN);
                        brilho.setRadius(20);
                        brilho.setSpread(0.5);
                        pecaVisual.setEffect(brilho);
                    }

                    casa.getChildren().add(pecaVisual);
                } else if (pecaSelecionada != null) {
                    Posicao destinoTeste = new Posicao(linha, coluna);
                    if (jogo.isJogadaValida(pecaSelecionada, destinoTeste)) {
                        Circle marcadorDestino = new Circle(10);
                        marcadorDestino.setFill(Color.PURPLE.brighter());
                        marcadorDestino.setOpacity(0.7);
                        casa.getChildren().add(marcadorDestino);
                    }
                }

                int l = linha;
                int c = coluna;
                casa.setOnMouseClicked(event -> processarCliqueNaCasa(l, c));

                tabuleiroGrid.add(casa, coluna, linha);
            }
        }
    }

    private void processarCliqueNaCasa(int linha, int coluna) {
        if (jogoFinalizado) return;
        Posicao clicada = new Posicao(linha, coluna);

        if (pecaSelecionada == null) {
            Peca pecaClicada = jogo.getPeca(linha, coluna);
            if (pecaClicada != Peca.VAZIA &&
                    pecaClicada == jogo.getTurnoAtual() &&
                    pecaClicada == minhaCor) {
                pecaSelecionada = clicada;
            }
        } else {
            if (pecaSelecionada.equals(clicada)) {
                pecaSelecionada = null;
            } else {
                boolean sucesso = jogo.tentarMovimento(pecaSelecionada, clicada);

                if (sucesso) {
                    System.out.println("Movimento executado!");

                    // Empacotamento rigoroso de coordenadas com o Builder
                    PacoteRede pacote = PacoteRede.newBuilder()
                            .setMovimento(Movimento.newBuilder()
                                    .setLinhaOrigem(pecaSelecionada.linha())
                                    .setColunaOrigem(pecaSelecionada.coluna())
                                    .setLinhaDestino(clicada.linha())
                                    .setColunaDestino(clicada.coluna())
                                    .build())
                            .build();

                    enviarPacoteRede(pacote);

                    Peca vencedor = jogo.verificarVencedor();
                    if (vencedor != null) {
                        anunciarVencedor(vencedor);
                    }
                } else {
                    System.out.println("Movimento inválido.");
                }

                pecaSelecionada = null;
            }
        }
        atualizarTabuleiroNaTela();
        atualizarStatusTurno();
    }

    private void anunciarVencedor(Peca vencedor) {
        jogoFinalizado = true;

        String nomeVencedor = (vencedor == Peca.BRANCA) ? "Brancas" : "Pretas";

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Fim de Jogo!");
        alerta.setHeaderText("Temos um vencedor!");
        alerta.setContentText("A equipa das peças " + nomeVencedor + " capturou todas as peças adversárias e venceu a partida!");

        alerta.showAndWait();
    }

    private void anunciarDesistencia(Peca vencedor, boolean fuiEuQuemDesistiu) {
        jogoFinalizado = true;

        statusTurno.setText("Partida Encerrada");
        statusTurno.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #7f8c8d;");

        String mensagem = fuiEuQuemDesistiu ?
                "Você desistiu da partida. O adversário venceu!" :
                "O adversário abandonou o jogo. Você venceu por desistência!";

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Fim de Jogo!");
        alerta.setHeaderText(fuiEuQuemDesistiu ? "Derrota" : "Vitória!");
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }
}
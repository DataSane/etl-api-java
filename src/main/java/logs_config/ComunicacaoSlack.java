package logs_config;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ComunicacaoSlack {
    private static final HttpClient client = HttpClient.newHttpClient();

    // URL do webhook do Slack (substitua pelo seu webhook real)
    private static final String webhookCanalLog = "https://hooks.slack.com/services/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";

    public static void enviarMensagem(int level, String mensagem) {
        // Define a descrição do log com base no nível
        String descricao;
        switch (level) {
            case 1:
                descricao = "SEVERE: Falha crítica detectada no sistema.";
                break;
            case 2:
                descricao = "WARNING: Alerta de possível problema.";
                break;
            case 3:
                descricao = "INFO: Informações gerais do sistema.";
                break;
            case 4:
                descricao = "CONFIG: Configuração aplicada.";
                break;
            case 5:
                descricao = "FINE: Log detalhado para depuração.";
                break;
            case 6:
                descricao = "FINER: Log refinado para análise.";
                break;
            case 7:
                descricao = "FINEST: Log mais detalhado capturado.";
                break;
            default:
                descricao = "UNKNOWN: Nível de log não reconhecido.";
        }

        // Monta a mensagem final
        String mensagemFinal = String.format("%s\nDescrição: %s", mensagem, descricao);

        try {
            // Cria o objeto JSON com a mensagem
            JSONObject json = new JSONObject();
            json.put("text", mensagemFinal);

            // Envia a mensagem para o Slack
            HttpRequest request = HttpRequest.newBuilder(URI.create(webhookCanalLog))
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            // Envia a requisição e exibe a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.printf("Mensagem enviada para o Slack: Status %d%n", response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.err.printf("Erro ao enviar mensagem para o Slack: %s%n", e.getMessage());
            e.printStackTrace();
        }
    }
}

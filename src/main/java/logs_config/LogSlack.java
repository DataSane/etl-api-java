package logs_config;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;

public class LogSlack extends Log{
    private static final HttpClient client = HttpClient.newHttpClient();

    // URL do webhook do Slack (substitua pelo seu webhook real)
    private static final String webhookCanalLog = "https://hooks.slack.com/services/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";

    @Override
    public void setLog(Integer level, String logName, String className) {

        Level logLevel;

        switch (level) {
            case 1:
                logLevel = Level.SEVERE;
                break;
            case 2:
                logLevel = Level.WARNING;
                break;
            case 3:
                logLevel = Level.INFO;
                break;
            case 4:
                logLevel = Level.CONFIG;
                break;
            case 5:
                logLevel = Level.FINE;
                break;
            case 6:
                logLevel = Level.FINER;
                break;
            case 7:
                logLevel = Level.FINEST;
                break;
            default:
                logLevel = Level.ALL;
                break;
        }

        String mensagemFinal = String.format("%s %s", logLevel, logName);

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

package logs_config;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {
        // Cria o executor para agendar a tarefa
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Definir o intervalo de 1 minuto (60 segundos)
        scheduler.scheduleAtFixedRate(() -> {
            // Cria uma instância da classe Random
            Random random = new Random();

            // Gera um número aleatório entre 1 e 7 (níveis de log)
            int logLevel = random.nextInt(7) + 1;

            // Mensagens diferentes para cada nível de log
            String mensagem = "";
            switch (logLevel) {
                case 1:
                    mensagem = "Erro grave no sistema. Reinicialização necessária.";
                    break;
                case 2:
                    mensagem = "Aviso: possível problema detectado.";
                    break;
                case 3:
                    mensagem = "Sistema funcionando normalmente.";
                    break;
                case 4:
                    mensagem = "Configuração aplicada com sucesso.";
                    break;
                case 5:
                    mensagem = "Log detalhado para análise de desempenho.";
                    break;
                case 6:
                    mensagem = "Log refinado: detalhes extras para análise.";
                    break;
                case 7:
                    mensagem = "Log de depuração profunda.";
                    break;
            }

            // Enviar a mensagem para o Slack diretamente
            ComunicacaoSlack.enviarMensagem(logLevel, mensagem);
        }, 0, 1, TimeUnit.MINUTES);  // Inicia imediatamente e repete a cada 1 minuto
    }
}

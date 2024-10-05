package logs_config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHandler {
    public void setLog(Integer level, String logName, String className) {

        // Instancia um Logger para a classe MainLogging
        Logger logger = Logger.getLogger(LogHandler.class.getName());
        logger.setUseParentHandlers(false); // fazendo não printar no console, pra não duplicar a saída no console


        // Declarar o FileHandler fora do try para garantir que ele esteja acessível no bloco finally
        FileHandler fileHandler = null;

        String filePath = "logs/%s_execution.log".formatted(className);

        try {
            // Instancia a classe que construirá o arquivo de logs
            // O parâmetro true permite que os logs sejam adicionados ao final do arquivo existente
            fileHandler = new FileHandler(filePath, true);

            // Define a formatação das mensagens de log
            fileHandler.setFormatter(new SimpleFormatter());

            // Adiciona o FileHandler ao Logger
            logger.addHandler(fileHandler);

            // Configura o nível de log com base no parâmetro fornecido
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
                    logger.warning("Unknown level of log");
                    break;
            }


            LocalDateTime nowDateTime = LocalDateTime.now(); // Obtém a data e hora atuais
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  // Define o formato desejado
            String dateHourFormatted = nowDateTime.format(formatter); // Formata a data e hora

            // Configura o nível do Logger
            logger.setLevel(logLevel);

            // Adiciona a mensagem ao log com o nível configurado + um espaçador "enter" de acordo com o sistema
            logger.log(logLevel, logName+System.lineSeparator());
            System.out.println("%s | [%s]: %s".formatted(dateHourFormatted,logLevel, logName));

        }catch(IOException e) {
            // Captura e imprime o stack trace se ocorrer uma IOException
            e.printStackTrace();
        } finally {
            // Garante que o FileHandler seja fechado, se foi criado
            if(fileHandler != null) {
                fileHandler.close();
            }
        }
    }
}
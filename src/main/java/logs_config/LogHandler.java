package logs_config;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class LogHandler extends Log{

    @Override
    public void setLog(Integer level, String logName, String className) {

        // Instancia um Logger para a classe LogHandler
        Logger logger = Logger.getLogger(LogHandler.class.getName());
        logger.setUseParentHandlers(true); // manter como true para permitir a impressão no console

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

        // Obtém a data e hora atuais
        LocalDateTime nowDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  // Define o formato desejado
        String dateHourFormatted = nowDateTime.format(formatter); // Formata a data e hora

        // Configura o nível do Logger
        logger.setLevel(logLevel);

        // Formata a mensagem para o log
        String logMessage = String.format("%s | [%s]: %s", dateHourFormatted, logLevel, logName);
        logger.log(logLevel, logMessage);

        // Exibe no console
        System.out.println(logMessage);

    }
}

package logs_config;

import client.clientMySQL.ControllerMySQL;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        LogSlack slack = new LogSlack();
        LogHandler terminal = new LogHandler();

        terminal.setLog(3, "Starting application...", Main.class.getName());
        slack.setLog(3, "Starting application...", Main.class.getName());

        ControllerMySQL controllerMySQL = new ControllerMySQL();


        controllerMySQL.createTables();
        controllerMySQL.insertMunicipios();

        // Cria o executor para agendar a tarefa
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Definir o intervalo de 1 minuto (60 segundos)
        scheduler.scheduleAtFixedRate(() -> {
            slack.setLog(7, "Running application successfully...", Main.class.getName());

        }, 0, 1, TimeUnit.MINUTES);  // Inicia imediatamente e repete a cada 1 minuto
    };
}
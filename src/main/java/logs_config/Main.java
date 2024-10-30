package logs_config;

import apache_configuration.GerenciadorMunicipio;
import client.clientMySQL.ControllerMySQL;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        ControllerMySQL controllerMySQL = new ControllerMySQL();
        GerenciadorMunicipio gerenciador = new GerenciadorMunicipio();
//
//        gerenciador.criar();
//        System.out.println(gerenciador.calculateAverage("populacaoSemColetaDeLixo"));
        controllerMySQL.createMunicipios();
        controllerMySQL.insertMunicipios();
//        controllerMySQL.selectMunicipios();
    };
}

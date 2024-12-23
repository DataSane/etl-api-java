package client.clientMySQL;

import apache_configuration.GerenciadorMunicipio;
import apache_configuration.Municipio;
import client.bucketS3.ControllerBucket;
import logs_config.LogHandler;
import logs_config.LogSlack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ControllerMySQL {
    ConnectionMySQL conexao = new ConnectionMySQL();
    JdbcTemplate con = conexao.getConexao();
    GerenciadorMunicipio gerenciadorMunicipio = new GerenciadorMunicipio();
    LogHandler terminal = new LogHandler(); // intancia, pra usar o método
    LogSlack slack = new LogSlack();

    String nameDatabase = "datasane";
    String nameTable = null;



    List<Municipio> listaMunicipios = new ArrayList<>();

    public void createTables() {

        terminal.setLog(3, "CREATING DATABASE IF NOT EXISTS", ControllerMySQL.class.getName());
        slack.setLog(3, "CREATING DATABASE IF NOT EXISTS", ControllerMySQL.class.getName());

        con.execute("CREATE DATABASE IF NOT EXISTS %s".formatted(nameDatabase));
        con.execute("USE %s".formatted(nameDatabase));

        String createTable = null;

        con.execute("DROP TABLE IF EXISTS %s".formatted("agrupamentoMunicipios"));
        for (int table = 1; table <= 3; table++) {
            switch (table) {
                case 1:
                    nameTable = "municipio";
                    createTable = """
                            CREATE TABLE municipio (
                                idMunicipio INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                            	nome VARCHAR(60),
                            	populacaoTotal INT,
                                populacaoSemLixo DECIMAL(4,2),
                                populacaoSemAgua DECIMAL(4,2),
                                populacaoSemEsgoto DECIMAL(4,2),
                                domicilioSujeitoInundacoes DECIMAL(4,2),
                                possuiPlanoMunicipal VARCHAR(15)
                            );
                            """.formatted(nameTable);
                    break;
                case 2:
                    nameTable = "tipoMunicipio";
                    createTable = """
                            CREATE TABLE %s (
                                idTipoMunicipio INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                                nome VARCHAR(30),
                                populacaoMin INT,
                                populacaoMax INT,
                                parametroSemColetaDeLixo DECIMAL(4,2),
                                parametroSemAgua DECIMAL(4,2),
                                parametroSemEsgoto DECIMAL(4,2)
                            );
                            """.formatted(nameTable);
                    break;
                case 3:
                    nameTable = "agrupamentoMunicipios";
                    createTable = """
                            CREATE TABLE %s (
                                idAgrupamentoMunicipios INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                                fkMunicipio INT NOT NULL,
                                fkTipoMunicipio INT NOT NULL,
                                tipoAgrupamento ENUM("geral", "porte"),
                                FOREIGN KEY (fkMunicipio) REFERENCES municipio(idMunicipio),
                                FOREIGN KEY (fkTipoMunicipio) REFERENCES tipoMunicipio(idTipoMunicipio)
                            );
                            """.formatted(nameTable);
            }

            con.execute("DROP TABLE IF EXISTS %s".formatted(nameTable));
            con.execute(createTable);

            terminal.setLog(3, "DROPING OR CREATE TABLE municipio, tipoMunicipio e agrupamentoMunicipios IF NOT EXISTS", ControllerMySQL.class.getName());
            slack.setLog(3, "DROPING OR CREATE TABLE municipio, tipoMunicipio e agrupamentoMunicipios IF NOT EXISTS", ControllerMySQL.class.getName());

        }
    }

    public void insertMunicipios() {
        terminal.setLog(3, "INSERTING municipios INTO DATABASE...", ControllerMySQL.class.getName());
        terminal.setLog(3, "INSERTING municipios INTO DATABASE...", ControllerMySQL.class.getName());

        gerenciadorMunicipio.criar();


        listaMunicipios = gerenciadorMunicipio.getMunicipios();

        Integer tipoMunicipio = null;

        for (Municipio municipio : listaMunicipios) {
            nameTable = "municipio";
            String sqlInsertScript = """
                    INSERT INTO %s VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)""".formatted(nameTable);

            con.update(sqlInsertScript, municipio.getMunicipio(), municipio.getPopulacao(), municipio.getPopulacaoSemColetaDeLixo(),
                    municipio.getPopulacaoSemAgua(), municipio.getPopulacaoSemEsgoto(), municipio.getDomiciliosSujeitosAInundacao(),
                    municipio.getPlanoMunicipal());
        }

        for (int counter = 1; counter <= 4; counter++) {
            nameTable = "tipoMunicipio";

            String groupName = null;
            Integer minPopulation = 0;
            Integer maxPopulation = 100_000_000;

            Double semColetaDeLixoParameter = null;
            Double semAguaParameter = null;
            Double semEsgotoParameter = null;

            switch (counter) {
                case 1:
                    groupName = "Geral";
                    tipoMunicipio = 1;
                    semColetaDeLixoParameter = gerenciadorMunicipio.calculateAverage("populacaoSemColetaDeLixo", "geral");
                    semAguaParameter = gerenciadorMunicipio.calculateAverage("populacaoSemAgua", "geral");
                    semEsgotoParameter = gerenciadorMunicipio.calculateAverage("populacaoSemEsgoto", "geral");
                    break;
                case 2:
                    groupName = "Pequeno Porte";
                    tipoMunicipio = 2;
                    maxPopulation = 50_000;
                    semColetaDeLixoParameter = gerenciadorMunicipio.calculateAverage("populacaoSemColetaDeLixo", "pequeno");
                    semAguaParameter = gerenciadorMunicipio.calculateAverage("populacaoSemAgua", "pequeno");
                    semEsgotoParameter = gerenciadorMunicipio.calculateAverage("populacaoSemEsgoto", "pequeno");
                    break;
                case 3:
                    groupName = "Médio Porte";
                    tipoMunicipio = 3;
                    minPopulation = 50_001;
                    maxPopulation = 200_000;
                    semColetaDeLixoParameter = gerenciadorMunicipio.calculateAverage("populacaoSemColetaDeLixo", "medio");
                    semAguaParameter = gerenciadorMunicipio.calculateAverage("populacaoSemAgua", "medio");
                    semEsgotoParameter = gerenciadorMunicipio.calculateAverage("populacaoSemEsgoto", "medio");
                    break;
                case 4:
                    groupName = "Grande Porte";
                    tipoMunicipio = 4;
                    semColetaDeLixoParameter = gerenciadorMunicipio.calculateAverage("populacaoSemColetaDeLixo", "grande");
                    semAguaParameter = gerenciadorMunicipio.calculateAverage("populacaoSemAgua", "grande");
                    semEsgotoParameter = gerenciadorMunicipio.calculateAverage("populacaoSemEsgoto", "grande");
                    minPopulation = 200_001;
            }

            String sqlInsertScript = """
                    INSERT INTO %s VALUES (DEFAULT, "%s", %d, %d, ?, ?, ?)""".formatted(nameTable, groupName, minPopulation, maxPopulation);

            con.update(sqlInsertScript, semColetaDeLixoParameter, semAguaParameter, semEsgotoParameter);
        }

        for (int municipios = 0; municipios < listaMunicipios.size(); municipios++) {
            nameTable = "agrupamentoMunicipios";
            Integer qtdPopulacao = listaMunicipios.get(municipios).getPopulacao();

            if (qtdPopulacao <= 50_000) {
                tipoMunicipio = 2;
            } else if (qtdPopulacao <= 200_000) {
                tipoMunicipio = 3;
            } else if (qtdPopulacao >= 200_001) {
                tipoMunicipio = 4;
            }

            String sqlInsertScript = """
                    INSERT INTO %s VALUES (DEFAULT, ?, ?, ?)""".formatted(nameTable);

            int idMunicipio = municipios + 1;
            con.update(sqlInsertScript, idMunicipio, 1, "geral");
            con.update(sqlInsertScript, idMunicipio, tipoMunicipio, "porte");
        }

        terminal.setLog(3, "Transforming CSV file into Object municipios...", ControllerBucket.class.getName());
        slack.setLog(3, "Transforming CSV file into Object municipios...", ControllerBucket.class.getName());

        terminal.setLog(3, "Formatting data from Object....", ControllerMySQL.class.getName());
        slack.setLog(3, "Formatting data from Object...", ControllerMySQL.class.getName());

        terminal.setLog(3, "Preparing INSERT SQL Query...", ControllerMySQL.class.getName());
        slack.setLog(3, "Preparing INSERT SQL Query...", ControllerMySQL.class.getName());

        terminal.setLog(3, "EVERYTHING HAS BEEN INSERTED ON DATABASE!", ControllerMySQL.class.getName());
        slack.setLog(3, "EVERYTHING HAS BEEN INSERTED ON DATABASE!", ControllerMySQL.class.getName());

    }
}

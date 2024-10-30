package client.clientMySQL;

import apache_configuration.GerenciadorMunicipio;
import apache_configuration.Municipio;
import logs_config.LogHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ControllerMySQL {
    ConnectionMySQL conexao = new ConnectionMySQL();
    JdbcTemplate con = conexao.getConexao();
    GerenciadorMunicipio gerenciadorMunicipio = new GerenciadorMunicipio();
    LogHandler mainLogger = new LogHandler(); // intancia, pra usar o método

    Boolean porcentagemAplicada = true;
    String nameDatabase = "datasaneTESTE";
    ;
    String nameTable = null;

    List<Municipio> listaMunicipios = new ArrayList<>();

    public void createMunicipios() {
        mainLogger.setLog(3, "DROP, CREATE e USE database", ControllerMySQL.class.getName());
        con.execute("CREATE DATABASE IF NOT EXISTS %s".formatted(nameDatabase));
        con.execute("USE %s".formatted(nameDatabase));

        mainLogger.setLog(3, "DROP e CREATE TABLE municipios e indicadores", ControllerMySQL.class.getName());
        for (Integer table = 1; table <= 2; table++) {
            nameTable = "municipios";
            con.execute("DROP TABLE IF EXISTS %s".formatted(nameTable));
            String createTable = """
                    CREATE TABLE %s (
                     idMunicipios INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                     nome VARCHAR(60),
                     populacaoTotal INT,
                     populacaoSemLixo DECIMAL(4,2),
                     populacaoSemAgua DECIMAL(4,2),
                     populacaoSemEsgoto DECIMAL(4,2),
                     domicilioSujeitoInundacoes DECIMAL(4,2),
                     possuiPlanoMunicipal VARCHAR(15));
                    """.formatted(nameTable);

            if (table.equals(2)) {
                nameTable = "indicadores";
                createTable = """
                        CREATE TABLE %s (
                        idIndicadores INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                        nome VARCHAR(20),
                        porcentagem DECIMAL(4,2));
                        """.formatted(nameTable);
            }


            con.execute(createTable);
        }
    }

    public void insertMunicipios() {
        mainLogger.setLog(3, "Iniciando inserção dos objetos no Banco", ControllerMySQL.class.getName());

        for (Integer table = 1; table <= 2; table++) {

            if (table.equals(1)) {
                try {
                    gerenciadorMunicipio.criar();
                    nameTable = "municipiosBase";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    gerenciadorMunicipio.criar();
                    nameTable = "municipiosTratada";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            String sqlInsertScript = """
                    INSERT INTO %s VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)""".formatted(nameTable);


            for (Municipio municipio : listaMunicipios) {
                con.update(sqlInsertScript, municipio.getMunicipio(), municipio.getPopulacao(), municipio.getPopulacaoSemColetaDeLixo(),
                        municipio.getPopulacaoSemAgua(), municipio.getPopulacaoSemEsgoto(), municipio.getDomiciliosSujeitosAInundacao(), municipio.getPlanoMunicipal()
                );
            }
        }

        mainLogger.setLog(3, "Todos objetos inseridos no Banco", ControllerMySQL.class.getName());
    }

    public void selectMunicipios() {
        List<MunicipiosBaseSelectModel> municipiosBase = con.query("SELECT * FROM municipiosBase", new BeanPropertyRowMapper<>(MunicipiosBaseSelectModel.class));
        List<MunicipiosTratadaSelectModel> municipiosTratada = con.query("SELECT * FROM municipiosTratada", new BeanPropertyRowMapper<>(MunicipiosTratadaSelectModel.class));

        mainLogger.setLog(3, "Exibindo dados da tabela MySQL", ControllerMySQL.class.getName());

        for (Integer table = 1; table <= 2; table++) {
            if (table.equals(1)) {
                for (MunicipiosBaseSelectModel municipio : municipiosBase) {
                    System.out.println("""
                            idMunicipios: %d
                            nome: %s
                            populacaoTotal: %d
                            populacaoSemLixo: %f
                            populacaoSemAgua: %.2f
                            populacaoSemEsgoto: %.2f
                            domicilioSujeitoInundacoes: %.2f
                            possuiPlanoMunicipal: %s
                            """.formatted(municipio.getIdMunicipios(), municipio.getNome(), municipio.getPopulacaoTotal(), municipio.getPopulacaoSemLixo(),
                            municipio.getPopulacaoSemAgua(), municipio.getPopulacaoSemEsgoto(), municipio.getDomicilioSujeitoInundacoes(),
                            municipio.getPossuiPlanoMunicipal()));
                }
            } else {
                for (MunicipiosTratadaSelectModel municipio : municipiosTratada) {
                    System.out.println("""
                            idMunicipios: %d
                            nome: %s
                            populacaoTotal: %d
                            populacaoSemLixo: %d
                            populacaoSemAgua: %d
                            populacaoSemEsgoto: %d
                            domicilioSujeitoInundacoes: %.2f
                            possuiPlanoMunicipal: %s
                            """.formatted(municipio.getIdMunicipios(), municipio.getNome(), municipio.getPopulacaoTotal(), municipio.getPopulacaoSemLixo(),
                            municipio.getPopulacaoSemAgua(), municipio.getPopulacaoSemEsgoto(), municipio.getDomicilioSujeitoInundacoes(),
                            municipio.getPossuiPlanoMunicipal()));
                }
            }

        }

        mainLogger.setLog(3, "Exibição de objetos finalizada", ControllerMySQL.class.getName());
    }
}

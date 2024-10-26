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
    String nameDatabase = conexao.getNameDatabase();
    String nameTable = null;

    List<Municipio> listaMunicipios = new ArrayList<>();

    public void createMunicipios() {
        String sqlType = null;

        mainLogger.setLog(3, "DROP, CREATE e USE database", ControllerMySQL.class.getName());
        con.execute("CREATE DATABASE IF NOT EXISTS %s".formatted(nameDatabase));
        con.execute("USE %s".formatted(nameDatabase));

        mainLogger.setLog(3, "DROP e CREATE tabelas Municipios", ControllerMySQL.class.getName());
        for (Integer table = 1; table <= 2; table++) {
            sqlType = "DECIMAL(5,2)";
            nameTable = "municipiosBase";

            if (table.equals(2)) {
                sqlType = "INT";
                nameTable = "municipiosTratada";
            }

            con.execute("DROP TABLE IF EXISTS %s".formatted(nameTable));
            String createTable = """
                    CREATE TABLE %s(
                     idMunicipios INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                     nome VARCHAR(60),
                     populacaoTotal INT,
                     populacaoSemLixo %s,
                     populacaoSemAgua %s,
                     populacaoSemEsgoto %s,
                     domicilioSujeitoInundacoes DECIMAL(5,2),
                     possuiPlanoMunicipal VARCHAR(15));
                    """.formatted(nameTable, sqlType, sqlType, sqlType);

            con.execute(createTable);
        }
    }

    public void insertMunicipios() {
        try {
            listaMunicipios = gerenciadorMunicipio.criar(!porcentagemAplicada);
            listaMunicipios = gerenciadorMunicipio.criar(porcentagemAplicada);
            mainLogger.setLog(3, "Células lidas com sucesso", ControllerMySQL.class.getName());
        } catch (IOException ex) {
            mainLogger.setLog(1, ex.getMessage(), ControllerMySQL.class.getName());
        }

        mainLogger.setLog(3, "Iniciando inserção dos objetos no Banco", ControllerMySQL.class.getName());

        for (Integer table = 1; table <= 2; table++) {
            nameTable = "municipiosBase";

            if (table.equals(2)) {
                nameTable = "municipiosTratada";
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
                            populacaoSemLixo: %.2f
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
                            populacaoSemLixo: %s
                            populacaoSemAgua: %s
                            populacaoSemEsgoto: %s
                            domicilioSujeitoInundacoes: %s
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

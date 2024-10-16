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

    List<Municipio> listaMunicipios = new ArrayList<>();

    public void createMunicipios() {

        mainLogger.setLog(3,"DROP, CREATE e USE database", ControllerMySQL.class.getName());
        con.execute("USE datasaneBD");

        mainLogger.setLog(3,"DROP e CREATE tabela Municipios", ControllerMySQL.class.getName());
        con.execute("DROP TABLE IF EXISTS Municipios");
        con.execute("""
                CREATE TABLE Municipios(
                 idMunicipios INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                 nome VARCHAR(60),
                 populacaoTotal INT,
                 populacaoSemLixo DECIMAL(4,2),
                 populacaoSemAgua DECIMAL(4,2),
                 populacaoSemEsgoto DECIMAL(4,2),
                 domicilioSujeitoInundacoes DECIMAL(4,2),
                 possuiPlanoMunicipal VARCHAR(15));
                 """);
    }

    public void insertMunicipios() {
        try {
            listaMunicipios = gerenciadorMunicipio.criar();
            mainLogger.setLog(3,"Células lidas com sucesso", ControllerMySQL.class.getName());
        }catch (IOException ex){
            mainLogger.setLog(1,ex.getMessage(), ControllerMySQL.class.getName());
        }

        mainLogger.setLog(3,"Iniciando inserção dos objetos no Banco", ControllerMySQL.class.getName());

        for (Municipio municipio : listaMunicipios) {
            con.update("INSERT INTO Municipios VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)", municipio.getMunicipio(), municipio.getPopulacao(), municipio.getPopulacaoSemColetaDeLixo(),
                    municipio.getPopulacaoSemAgua(), municipio.getPopulacaoSemEsgoto(), municipio.getDomiciliosSujeitosAInundacao(), municipio.getPlanoMunicipal()
            );
        }

        mainLogger.setLog(3,"Todos objetos inseridos no Banco", ControllerMySQL.class.getName());
    }

    public void selectMunicipios() {
        List<MunicipiosSelectModel> municipios = con.query("SELECT * FROM Municipios", new BeanPropertyRowMapper<>(MunicipiosSelectModel.class));

        mainLogger.setLog(3,"Exibindo dados da tabela MySQL", ControllerMySQL.class.getName());
        for (MunicipiosSelectModel municipio : municipios) {
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
                    municipio.getPopulacaoSemAgua(), municipio.getPopulacaoSemEsgoto(), municipio.getDomicilioSujeitoInundacoes(), municipio.getPossuiPlanoMunicipal()));
        }
        mainLogger.setLog(3,"Exibição de objetos finalizada", ControllerMySQL.class.getName());
    }
}

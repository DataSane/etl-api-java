import apache_configuration.GerenciadorMunicipio;
import apache_configuration.Municipio;
import com.github.javafaker.Faker;
import logs_config.LogHandler;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexao();
        GerenciadorMunicipio gerenciadorMunicipio = new GerenciadorMunicipio();
        LogHandler mainLogger = new LogHandler(); // intancia, pra usar o método

        mainLogger.setLog(3,"DROP, CREATE e USE database", Main.class.getName());
        con.execute("DROP DATABASE datasaneBD");
        con.execute("CREATE DATABASE datasaneBD");
        con.execute("USE datasaneBD");

        mainLogger.setLog(3,"DROP e CREATE tabela Municipios", Main.class.getName());
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

        List<Municipio> listaMunicipios = new ArrayList<>();

        try {
            listaMunicipios = gerenciadorMunicipio.criar();
            mainLogger.setLog(3,"Células lidas com sucesso", Main.class.getName());
        }catch (IOException ex){
            mainLogger.setLog(1,ex.getMessage(), Main.class.getName());
        }

        mainLogger.setLog(3,"Iniciando inserção dos objetos no Banco", Main.class.getName());

        for (Municipio municipio : listaMunicipios) {
            con.update("INSERT INTO Municipios VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)", municipio.getMunicipio(), municipio.getPopulacao(), municipio.getPopulacaoSemColetaDeLixo(),
                    municipio.getPopulacaoSemAgua(), municipio.getPopulacaoSemEsgoto(), municipio.getDomiciliosSujeitosAInundacao(), municipio.getPlanoMunicipal()
            );
        }

        mainLogger.setLog(3,"Todos objetos inseridos no Banco", Main.class.getName());


        List<Municipios> municipios = con.query("SELECT * FROM Municipios", new BeanPropertyRowMapper<>(Municipios.class));

        mainLogger.setLog(3,"Exibindo dados da tabela MySQL", Main.class.getName());
        for (Municipios municipio : municipios) {
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
        mainLogger.setLog(3,"Exibição de objetos finalizada", Main.class.getName());
    };
}

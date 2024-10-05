import apache_configuration.GerenciadorMunicipio;
import apache_configuration.Municipio;
import com.github.javafaker.Faker;
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

        con.execute("DROP DATABASE datasaneBD");
        con.execute("CREATE DATABASE datasaneBD");
        con.execute("USE datasaneBD");

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
        }catch (IOException ex){
            System.out.println("ERRO:" + ex.getMessage());
        }




        for (Municipio municipio : listaMunicipios) {
            con.update("INSERT INTO Municipios VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)", municipio.getMunicipio(), municipio.getPopulacao(), municipio.getPopulacaoSemColetaDeLixo(),
                    municipio.getPopulacaoSemAgua(), municipio.getPopulacaoSemEsgoto(), municipio.getDomiciliosSujeitosAInundacao(), municipio.getPlanoMunicipal()
            );
        }

        List<Municipios> municipios = con.query("SELECT * FROM Municipios", new BeanPropertyRowMapper<>(Municipios.class));

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
    };
}

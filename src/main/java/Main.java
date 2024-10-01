import com.github.javafaker.Faker;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexao();

        con.execute("DROP DATABASE datasaneBD");
        con.execute("CREATE DATABASE datasaneBD");
        con.execute("USE datasaneBD");

        con.execute("DROP TABLE IF EXISTS Usuario");
        con.execute("""
                CREATE TABLE Usuario(
                idUsuario INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                nome VARCHAR(45) NOT NULL,
                email VARCHAR(45) NOT NULL,
                senha CHAR(8) NOT NULL);
                """);

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

        con.execute("DROP TABLE IF EXISTS Contato");
        con.execute("""
                CREATE TABLE Contato(
                idContato INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                email VARCHAR(60),
                assunto VARCHAR(60),
                mensagem VARCHAR(500));
                """);

        con.update("INSERT INTO Usuario VALUES (DEFAULT, 'Kauan Master', 'kauanMaster@datasane.com', 'kaMaster')");
        List<Usuario> usuarios = con.query("SELECT * FROM Usuario", new BeanPropertyRowMapper<>(Usuario.class));

        for (Usuario usuario : usuarios) {
            System.out.println("""
                    idUsuario: %d
                    nome: %s
                    email: %s   
                    senha: %s
                    """.formatted(usuario.getIdUsuario(), usuario.getNome(), usuario.getEmail(), usuario.getSenha()));
        }

        con.update("INSERT INTO Municipios VALUES (DEFAULT, 'Caieiras', 10.000, 25.00, 10.00, 15.00, 05.00, 'Sim')");
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

        con.update("INSERT INTO Contato VALUES (DEFAULT, 'kauanMaster@datasane.com', 'Conexão com Banco', 'Estou realizando CREATE, INSERT E SELECT')");
        List<Contato> contatos = con.query("SELECT * FROM Contato", new BeanPropertyRowMapper<>(Contato.class));
        for (Contato contato : contatos) {
            System.out.println("""
                    idContato: %d
                    email: %s
                    assunto: %s
                    mensagem: %s
                    """.formatted(contato.getIdContato(), contato.getEmail(), contato.getAssunto(), contato.getMensagem()));
        }
    };
}

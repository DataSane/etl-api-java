package client.clientMySQL;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class ConnectionMySQL {

    private final DataSource dataSource;

    public ConnectionMySQL() {
        BasicDataSource basicDataSource = new BasicDataSource();

//        basicDataSource.setUrl("jdbc:mysql://localhost/datasaneBD?useTimezone=true&serverTimezone=UTC");
//        basicDataSource.setUsername("datasane");
//        basicDataSource.setPassword("datasane2024");


//        // Construindo a URL do banco de dados a partir das variáveis de ambiente
        String dbHost = System.getenv("DB_HOST");
        String dbName = System.getenv("DB_NAME");
        String dbUrl = "jdbc:mysql://" + dbHost + ":3306/" + dbName;

        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(System.getenv("DB_USER"));
        basicDataSource.setPassword(System.getenv("DB_PASSWORD"));

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate getConexao() {
        return new JdbcTemplate(dataSource);
    }
}
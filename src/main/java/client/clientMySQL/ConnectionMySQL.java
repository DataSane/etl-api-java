package client.clientMySQL;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class ConnectionMySQL {

    private final DataSource dataSource;

    public ConnectionMySQL() {
        Dotenv dotenv = Dotenv.load();
        
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dotenv.get("MYSQL_URL"));
        basicDataSource.setUsername(dotenv.get("MYSQL_USERNAME"));
        basicDataSource.setPassword(dotenv.get("MYSQL_PASSWORD"));

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate getConexao() {
        return new JdbcTemplate(dataSource);
    }
}
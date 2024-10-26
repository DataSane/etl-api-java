package client.clientMySQL;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class ConnectionMySQL {
    private final DataSource dataSource;

    Dotenv dotenv = Dotenv.load();

    @Getter
    private final String nameDatabase = dotenv.get("MYSQL_DATABASE");

    public ConnectionMySQL() {
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
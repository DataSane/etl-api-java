import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class Conexao {

    private final DataSource dataSource;

    public Conexao() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl("jdbc:mysql://localhost/datasaneBD?useTimezone=true&serverTimezone=UTC");
        basicDataSource.setUsername("datasane");
        basicDataSource.setPassword("datasane2024");

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate getConexao() {
        return new JdbcTemplate(dataSource);
    }
}
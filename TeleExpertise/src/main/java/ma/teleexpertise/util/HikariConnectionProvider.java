package ma.teleexpertise.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;
import java.sql.*;
import java.util.Map;

public class HikariConnectionProvider implements ConnectionProvider, Configurable, Stoppable {
    private HikariDataSource dataSource;
    public void configure(Map<String, Object> values) {
        HikariConfig config = new HikariConfig();

        String url = (String) values.get("jakarta.persistence.jdbc.url");

        if (url.startsWith("jdbc:h2:")) {
            config.setDriverClassName("org.h2.Driver");
        } else if (url.startsWith("jdbc:postgresql:")) {
            config.setDriverClassName("org.postgresql.Driver");
        }

        config.setJdbcUrl(url);
        config.setUsername((String) values.get("jakarta.persistence.jdbc.user"));
        config.setPassword((String) values.get("jakarta.persistence.jdbc.password"));
        config.setMaximumPoolSize(5);
        config.setPoolName("TeleExpertise");

        dataSource = new HikariDataSource(config);
    }
    public Connection getConnection() throws SQLException { return dataSource.getConnection(); }
    public void closeConnection(Connection connection) throws SQLException { connection.close(); }
    public boolean supportsAggressiveRelease() { return false; }
    public boolean isUnwrappableAs(Class<?> type) { return type.isInstance(this); }
    public <T> T unwrap(Class<T> type) { return type.cast(this); }
    public void stop() { if (dataSource != null) dataSource.close(); }
}

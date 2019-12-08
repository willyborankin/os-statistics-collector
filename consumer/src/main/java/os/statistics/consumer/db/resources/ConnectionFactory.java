package os.statistics.consumer.db.resources;

import com.typesafe.config.Config;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);

    static  {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't register PostgreSQL driver", e);
        }
    }

    public static Connection createConnection(Config configuration) {
        final var jdbcUrl = configuration.getString("jdbc.db.url");
        final var dbUser = configuration.getString("jdbc.db.user");
        final var dbPassword = configuration.getString("jdbc.db.password");
        LOGGER.info("Create DB connection: JDBC URL: {}, user: {}, withPassword: {}", jdbcUrl, dbUser, dbPassword.isBlank());
        try {
            final var connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't create connection to db");
        }
    }

}

package os.statistics.consumer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(DbUtils.class);

    public static void closeQuietly(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.warn("Couldn't close connection", e);
        }
    }

    public static void closeQuietly(PreparedStatement ps) {
        try {
            ps.close();
        } catch (SQLException e) {
            LOGGER.warn("Couldn't close prepared statement ", e);
        }
    }
}

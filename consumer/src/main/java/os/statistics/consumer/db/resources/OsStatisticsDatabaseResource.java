package os.statistics.consumer.db.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import os.statistics.commons.model.Host;
import os.statistics.consumer.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class OsStatisticsDatabaseResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(OsStatisticsDatabaseResource.class);

    public final static String HOST_HARDWARE_INSERT_SQL =
            "INSERT INTO host_hardware(" +
                    "id, host, ip_address, " +
                    "cpu_utilization, system_load_average, free_physical_memory_size, " +
                    "total_physical_memory_size, free_swap_space_size, total_swap_space_size) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public final static String HOST_FILE_SYSTEM_INSERT_SQL =
            "INSERT INTO host_file_system(" +
                    "id, host, ip_address, " +
                    "path, total_space, " +
                    "usable_space, free_space " +
                    ") VALUES(?, ?, ?, ?, ?, ?, ?)";

    private final Connection connection;

    private final PreparedStatement hostHardwarePreparedStatement;

    private final PreparedStatement hostFileSystemPreparedStatement;

    public OsStatisticsDatabaseResource(Connection connection) {
        try {
            this.connection = connection;
            hostHardwarePreparedStatement = connection.prepareStatement(HOST_HARDWARE_INSERT_SQL);
            hostFileSystemPreparedStatement = connection.prepareStatement(HOST_FILE_SYSTEM_INSERT_SQL);
        } catch (SQLException sqle) {
            throw new RuntimeException("Couldn't prepare insert SQL statements", sqle);
        }
    }

    public void addStatistics(Host host) {
        try {
            insertHostHardwareStatistics(host);
            insertHostFileSystemStatistics(host);
            hostHardwarePreparedStatement.execute();
            hostFileSystemPreparedStatement.execute();
            connection.commit();
        } catch (SQLException sqle) {
            LOGGER.error("Couldn't insert data into database", sqle);
            try {
                connection.rollback();
            } catch (SQLException e) {
                ; //skip it
            }
            throw new RuntimeException("Couldn't process host info", sqle);
        }
    }

    private void insertHostHardwareStatistics(Host host) throws SQLException {
        hostHardwarePreparedStatement.setString(1, UUID.randomUUID().toString());
        hostHardwarePreparedStatement.setString(2, host.getHostname());
        hostHardwarePreparedStatement.setString(3, host.getIpAddress());
        final var hardware = host.getHardware();
        hostHardwarePreparedStatement.setLong(4, hardware.getCpuUtilization());
        hostHardwarePreparedStatement.setLong(5, hardware.getSystemLoadAverage());
        hostHardwarePreparedStatement.setLong(6, hardware.getFreePhysicalMemorySize());
        hostHardwarePreparedStatement.setLong(7, hardware.getTotalPhysicalMemorySize());
        hostHardwarePreparedStatement.setLong(8, hardware.getFreeSwapSpaceSize());
        hostHardwarePreparedStatement.setLong(9, hardware.getTotalSwapSpaceSize());
    }

    private void insertHostFileSystemStatistics(Host host) throws SQLException {
        for (final var fs : host.getFileSystem()) {
            hostFileSystemPreparedStatement.setString(1, UUID.randomUUID().toString());
            hostFileSystemPreparedStatement.setString(2, host.getHostname());
            hostFileSystemPreparedStatement.setString(3, host.getIpAddress());
            hostFileSystemPreparedStatement.setString(4, fs.getPath());
            hostFileSystemPreparedStatement.setLong(5, fs.getTotalSpace());
            hostFileSystemPreparedStatement.setLong(6, fs.getUsableSpace());
            hostFileSystemPreparedStatement.setLong(7, fs.getFreeSpace());
        }
    }

    public void close() {
        Utils.closeQuietly(hostHardwarePreparedStatement);
        Utils.closeQuietly(hostFileSystemPreparedStatement);
        Utils.closeQuietly(connection);
    }

}

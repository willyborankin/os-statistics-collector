package os.statistics.consumer.db.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import os.statistics.commons.model.FileSystem;
import os.statistics.commons.model.Hardware;
import os.statistics.commons.model.Host;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OsStatisticsDatabaseResourceTest {

    @Mock
    private Connection mockedConnection;

    @Mock
    private PreparedStatement mockedHardwarePreparedStatement;

    @Mock
    private PreparedStatement mockedFileSystemPreparedStatement;

    @Test
    public void shouldCreateObjectProperly() throws Exception {
        new OsStatisticsDatabaseResource(mockedConnection);

        verify(mockedConnection).prepareStatement(eq(OsStatisticsDatabaseResource.HOST_HARDWARE_INSERT_SQL));
        verify(mockedConnection).prepareStatement(eq(OsStatisticsDatabaseResource.HOST_FILE_SYSTEM_INSERT_SQL));
    }

    @Test
    void shouldAddStatistics() throws Exception {
        when(mockedConnection.prepareStatement(OsStatisticsDatabaseResource.HOST_HARDWARE_INSERT_SQL))
                .thenReturn(mockedHardwarePreparedStatement);
        when(mockedConnection.prepareStatement(OsStatisticsDatabaseResource.HOST_FILE_SYSTEM_INSERT_SQL))
                .thenReturn(mockedFileSystemPreparedStatement);

        final var hardware =
                new Hardware(1L, 2L, 3L, 4L, 5L, 6L);
        final var fileSystem =
                new FileSystem("/some_path", 7L, 8L,9L);
        final var host =
                new Host("some_ost", "some_ip_address")
                        .copy(hardware, List.of(fileSystem));
        new OsStatisticsDatabaseResource(mockedConnection)
                .addStatistics(host);

        verify(mockedHardwarePreparedStatement).setString(eq(1), any(String.class));
        verify(mockedHardwarePreparedStatement).setString(eq(2), eq(host.getHostname()));
        verify(mockedHardwarePreparedStatement).setString(eq(3), eq(host.getIpAddress()));

        verify(mockedHardwarePreparedStatement).setLong(eq(4), eq(hardware.getCpuUtilization()));
        verify(mockedHardwarePreparedStatement).setLong(eq(5), eq(hardware.getSystemLoadAverage()));
        verify(mockedHardwarePreparedStatement).setLong(eq(6), eq(hardware.getFreePhysicalMemorySize()));
        verify(mockedHardwarePreparedStatement).setLong(eq(7), eq(hardware.getTotalPhysicalMemorySize()));
        verify(mockedHardwarePreparedStatement).setLong(eq(8), eq(hardware.getFreeSwapSpaceSize()));
        verify(mockedHardwarePreparedStatement).setLong(eq(9), eq(hardware.getTotalSwapSpaceSize()));


        verify(mockedFileSystemPreparedStatement).setString(eq(1), any(String.class));
        verify(mockedFileSystemPreparedStatement).setString(eq(2), eq(host.getHostname()));
        verify(mockedFileSystemPreparedStatement).setString(eq(3), eq(host.getIpAddress()));

        verify(mockedFileSystemPreparedStatement).setString(eq(4), eq(fileSystem.getPath()));
        verify(mockedFileSystemPreparedStatement).setLong(eq(5), eq(fileSystem.getTotalSpace()));
        verify(mockedFileSystemPreparedStatement).setLong(eq(6), eq(fileSystem.getUsableSpace()));
        verify(mockedFileSystemPreparedStatement).setLong(eq(7), eq(fileSystem.getFreeSpace()));

        verify(mockedHardwarePreparedStatement).execute();
        verify(mockedFileSystemPreparedStatement).execute();

        verify(mockedConnection).commit();
    }

    @Test
    void shouldCloseResources() throws Exception {
        when(mockedConnection.prepareStatement(OsStatisticsDatabaseResource.HOST_HARDWARE_INSERT_SQL))
                .thenReturn(mockedHardwarePreparedStatement);
        when(mockedConnection.prepareStatement(OsStatisticsDatabaseResource.HOST_FILE_SYSTEM_INSERT_SQL))
                .thenReturn(mockedFileSystemPreparedStatement);

        new OsStatisticsDatabaseResource(mockedConnection).close();

        verify(mockedFileSystemPreparedStatement).close();
        verify(mockedHardwarePreparedStatement).close();
        verify(mockedConnection).close();
    }
}
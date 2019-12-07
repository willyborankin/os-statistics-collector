package os.statistics.producer;

import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;

class HostFactoryTest {

    @Test
    public void usesHostNameAndIpAddressFromConfiguration() {
        final var host = HostFactory.createHost(ConfigFactory.load("host-test.conf"));
        assertEquals("some-host-name", host.getHostname());
        assertEquals("10.20.30.40", host.getIpAddress());
    }

    @Test
    public void usesLocalhostHostNameAndIpAddressFromConfiguration() throws Exception {
        final var host = HostFactory.createHost(ConfigFactory.load("no-host-name.conf"));
        assertEquals(InetAddress.getLocalHost().getHostName(), host.getHostname());
        assertEquals("10.20.30.40", host.getIpAddress());
    }

    @Test
    public void throwsIllegalArgumentExceptionForEmptyHostName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> HostFactory.createHost(ConfigFactory.load("empty-host-test.conf"))
        );
    }

    @Test
    public void throwsIllegalArgumentExceptionForEmptyIpAddress() {
        assertThrows(
                IllegalArgumentException.class,
                () -> HostFactory.createHost(ConfigFactory.load("empty-ip-address-test.conf"))
        );
    }

}
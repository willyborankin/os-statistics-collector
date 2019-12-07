package os.statistics.producer;

import com.typesafe.config.Config;
import os.statistics.commons.model.Host;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class HostFactory {

    public static Host createHost(Config configuration) {
        final var hostname = determineHostName(configuration);
        final var ipAddress = getIpAddress(configuration);
        return new Host(hostname, ipAddress);
    }

    private static String determineHostName(Config configuration) {
        if (configuration.hasPath("os.collector.hostname"))
            return getAndAssertConfigParameter("os.collector.hostname", configuration);
        else {
            try {
                final var possibleHostname = InetAddress.getLocalHost().getHostName();
                if (possibleHostname.startsWith("localhost"))
                    throw new RuntimeException("Your default host name is localhost. Please provide the hostname in the configuration file");
                return possibleHostname;
            } catch (UnknownHostException e) {
                throw new RuntimeException("Couldn't determine host name. Please provide the hostname in the configuration file", e);
            }
        }
    }

    private static String getIpAddress(Config configuration) {
        return getAndAssertConfigParameter("os.collector.ipAddress", configuration);
    }

    private static String getAndAssertConfigParameter(String path, Config configuration) {
        final var value = configuration.getString(path);
        if (value.isEmpty() || value.isBlank())
            throw new IllegalArgumentException(String.format("Value for the config parameter: %s is empty or blank", path));
        return value;
    }


}

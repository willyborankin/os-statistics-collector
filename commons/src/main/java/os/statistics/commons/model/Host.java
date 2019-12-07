package os.statistics.commons.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Host {

    private final String hostname;

    private final String ipAddress;

    private final Hardware hardware;

    private final List<FileSystem> fileSystem;


    protected Host() {
        this(null, null);
    }

    public Host(String hostname, String ipAddress) {
        this(hostname, ipAddress, null, null);
    }

    private Host(String hostname,
                String ipAddress,
                Hardware hardware,
                List<FileSystem> fileSystem) {
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.hardware = hardware;
        this.fileSystem = fileSystem;
    }

    @JsonProperty
    public String getHostname() {
        return hostname;
    }

    @JsonProperty
    public String getIpAddress() {
        return ipAddress;
    }

    @JsonProperty
    public Hardware getHardware() {
        return hardware;
    }

    @JsonProperty
    public List<FileSystem> getFileSystem() {
        return fileSystem;
    }

    @JsonIgnore
    public Host copy(Hardware hardware,
                     List<FileSystem> fileSystem) {
        return new Host(hostname, ipAddress, hardware, fileSystem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host host = (Host) o;
        return Objects.equals(hostname, host.hostname) &&
                Objects.equals(ipAddress, host.ipAddress) &&
                Objects.equals(hardware, host.hardware) &&
                Objects.equals(fileSystem, host.fileSystem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostname, ipAddress, hardware, fileSystem);
    }

    @Override
    public String toString() {
        return "Host{" +
                "hostname='" + hostname + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", hardware=" + hardware +
                ", fileSystem=" + fileSystem +
                '}';
    }
}

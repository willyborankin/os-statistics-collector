package os.statistics.commons.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Hardware {

    private final long cpuUtilization;

    private final long systemLoadAverage;

    private final long freePhysicalMemorySize;

    private final long totalPhysicalMemorySize;

    private final long freeSwapSpaceSize;

    private final long totalSwapSpaceSize;

    protected Hardware() {
        this(0L, 0L, 0L, 0L, 0L, 0L);
    }

    public Hardware(long cpuUtilization,
                    long systemLoadAverage,
                    long freePhysicalMemorySize,
                    long totalPhysicalMemorySize,
                    long freeSwapSpaceSize,
                    long totalSwapSpaceSize) {
        this.cpuUtilization = cpuUtilization;
        this.systemLoadAverage = systemLoadAverage;
        this.freePhysicalMemorySize = freePhysicalMemorySize;
        this.totalPhysicalMemorySize = totalPhysicalMemorySize;
        this.freeSwapSpaceSize = freeSwapSpaceSize;
        this.totalSwapSpaceSize = totalSwapSpaceSize;
    }

    @JsonProperty
    public long getCpuUtilization() {
        return cpuUtilization;
    }

    @JsonProperty
    public long getSystemLoadAverage() {
        return systemLoadAverage;
    }

    @JsonProperty
    public long getFreePhysicalMemorySize() {
        return freePhysicalMemorySize;
    }

    @JsonProperty
    public long getTotalPhysicalMemorySize() {
        return totalPhysicalMemorySize;
    }

    @JsonProperty
    public long getFreeSwapSpaceSize() {
        return freeSwapSpaceSize;
    }

    @JsonProperty
    public long getTotalSwapSpaceSize() {
        return totalSwapSpaceSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hardware hardware = (Hardware) o;
        return cpuUtilization == hardware.cpuUtilization &&
                systemLoadAverage == hardware.systemLoadAverage &&
                freePhysicalMemorySize == hardware.freePhysicalMemorySize &&
                totalPhysicalMemorySize == hardware.totalPhysicalMemorySize &&
                freeSwapSpaceSize == hardware.freeSwapSpaceSize &&
                totalSwapSpaceSize == hardware.totalSwapSpaceSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpuUtilization, systemLoadAverage, freePhysicalMemorySize, totalPhysicalMemorySize, freeSwapSpaceSize, totalSwapSpaceSize);
    }

    @Override
    public String toString() {
        return "Hardware{" +
                "cpuUtilization=" + cpuUtilization +
                ", systemLoadAverage=" + systemLoadAverage +
                ", freePhysicalMemorySize=" + freePhysicalMemorySize +
                ", totalPhysicalMemorySize=" + totalPhysicalMemorySize +
                ", freeSwapSpaceSize=" + freeSwapSpaceSize +
                ", totalSwapSpaceSize=" + totalSwapSpaceSize +
                '}';
    }
}

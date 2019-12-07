package os.statistics.producer.statistics;

import com.sun.management.OperatingSystemMXBean;
import os.statistics.commons.model.Hardware;

import java.lang.management.ManagementFactory;

public class HardwareStatisticsCollector {

    private final OperatingSystemMXBean osMXBean;

    public HardwareStatisticsCollector() {
        this.osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    public Hardware loadHardwareInfo() {
        return new Hardware(
                calculateCpuUtilization(),
                calculateSystemLoadAverage(),
                getFreeMemoryInBytes(),
                getTotalMemoryInBytes(),
                getFreeSwapSpaceSizeInBytes(),
                getTotalSwapSpaceSizeInBytes()
        );
    }

    private long calculateCpuUtilization() {
        return (long) osMXBean.getProcessCpuLoad() * 100;
    }

    private long calculateSystemLoadAverage() {
        return (long) osMXBean.getSystemLoadAverage() * 100;
    }

    private long getFreeMemoryInBytes() {
        return osMXBean.getFreePhysicalMemorySize();
    }

    private long getTotalMemoryInBytes() {
        return osMXBean.getTotalPhysicalMemorySize();
    }

    private long getFreeSwapSpaceSizeInBytes() {
        return osMXBean.getFreeSwapSpaceSize();
    }

    private long getTotalSwapSpaceSizeInBytes() {
        return osMXBean.getTotalSwapSpaceSize();
    }

}

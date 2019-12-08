package os.statistics.producer.health;

import os.statistics.commons.health.ApplicationHealthMBean;

import java.util.concurrent.atomic.AtomicLong;

public class StatisticsProducerApplicationHealth implements StatisticsProducerApplicationHealthMBean {

    public final static String APPLICATION_NAME = "OS-STATISTICS-PRODUCER";

    private final AtomicLong successfulSent = new AtomicLong(0L);

    private final AtomicLong failedToSend = new AtomicLong(0L);

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Override
    public long getSuccessfulSent() {
        return successfulSent.get();
    }

    @Override
    public long getFailedToSend() {
        return failedToSend.get();
    }

    public void incrementSuccessfulSent() {
        successfulSent.incrementAndGet();
    }

    public void incrementFailedToSend() {
        failedToSend.incrementAndGet();
    }


}

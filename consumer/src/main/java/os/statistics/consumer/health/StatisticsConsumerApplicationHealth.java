package os.statistics.consumer.health;

import java.util.concurrent.atomic.AtomicLong;

public class StatisticsConsumerApplicationHealth implements StatisticsConsumerApplicationHealthMBean {

    public final static String APPLICATION_NAME = "OS-STATISTICS-CONSUMER";

    private final AtomicLong consumedMessages = new AtomicLong(0L);

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Override
    public long getConsumedMessages() {
        return consumedMessages.get();
    }

    public void incrementConsumedMessages(int amount) {
        consumedMessages.addAndGet(amount);
    }
}

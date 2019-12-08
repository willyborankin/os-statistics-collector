package os.statistics.consumer;

import com.typesafe.config.Config;
import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import os.statistics.commons.model.Host;
import os.statistics.commons.utils.Utils;
import os.statistics.consumer.db.resources.ConnectionFactory;
import os.statistics.consumer.db.resources.OsStatisticsDatabaseResource;
import os.statistics.consumer.health.StatisticsConsumerApplicationHealth;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

public class ConsumerWorker implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConsumerWorker.class);

    private final Consumer<UUID, Host> consumer;

    private final Duration pollInMillis;

    private final OsStatisticsDatabaseResource osStatisticsDatabaseResource;

    private StatisticsConsumerApplicationHealth applicationHealth;

    private final AtomicBoolean stop = new AtomicBoolean(false);

    protected ConsumerWorker(Consumer<UUID, Host> consumer,
                             OsStatisticsDatabaseResource osStatisticsDatabaseResource,
                             Duration pollInMillis) {
        requireNonNull(consumer, "Consumer has not been set");
        requireNonNull(consumer, "DB connection has not been set");
        this.consumer = consumer;
        this.osStatisticsDatabaseResource = osStatisticsDatabaseResource;
        this.pollInMillis = pollInMillis;
    }

    public static ConsumerWorker from(Config configuration) {
        requireNonNull(configuration, "Configuration has not been set");
        final var databaseResource =
                new OsStatisticsDatabaseResource(ConnectionFactory.createConnection(configuration));
        return new ConsumerWorker(
                ConsumerFactory.createConsumer(configuration),
                databaseResource,
                configuration.getDuration("consumer.poll")
        );
    }

    public ConsumerWorker withApplicationHealth(StatisticsConsumerApplicationHealth applicationHealth) {
        this.applicationHealth = applicationHealth;
        return this;
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    var counter = 0;
                    for (final var record : consumer.poll(pollInMillis)) {
                        osStatisticsDatabaseResource.addStatistics(record.value());
                        counter++;
                    }
                    consumer.commitSync();
                    final var msgCounter = counter;
                    Utils.executeIfNotNull(applicationHealth, () -> applicationHealth.incrementConsumedMessages(msgCounter));
                    if (stop.get()) break;
                } catch (Exception e) {
                    LOGGER.error("Couldn't process message", e);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            consumer.close();
        }

    }

    public void stopProcessing() {
        stop.getAndSet(true);
        consumer.wakeup();
    }

}

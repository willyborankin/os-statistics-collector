package os.statistics.producer.statistics;

import com.typesafe.config.Config;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.InvalidTopicException;
import org.apache.kafka.common.errors.OffsetMetadataTooLarge;
import org.apache.kafka.common.errors.RecordBatchTooLargeException;
import org.apache.kafka.common.errors.UnknownServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import os.statistics.commons.model.Host;
import os.statistics.commons.utils.Utils;
import os.statistics.producer.HostFactory;
import os.statistics.producer.ProducerFactory;
import os.statistics.producer.health.StatisticsProducerApplicationHealth;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.*;

public class OsStatisticsProvider {

    private final static Logger LOGGER = LoggerFactory.getLogger(OsStatisticsProvider.class);

    private final static int INITIAL_DELAY_SECONDS = 2;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private final Producer<UUID, Host> statisticsProducer;

    private final HardwareStatisticsCollector hardwareStatisticsCollector = new HardwareStatisticsCollector();

    private final FileSystemStatisticsCollector fileSystemStatisticsCollector = new FileSystemStatisticsCollector();

    private final String kafkaTopicName;

    private final Host host;

    private StatisticsProducerApplicationHealth applicationHealth;

    protected OsStatisticsProvider(Host host,
                                 String kafkaTopicName,
                                 Producer<UUID, Host> statisticsProducer) {
        this.host = host;
        this.kafkaTopicName = kafkaTopicName;
        this.statisticsProducer = statisticsProducer;
    }

    public static OsStatisticsProvider from(Config configuration) {
        requireNonNull(configuration, "Configuration has not been set");
        LOGGER.info("Create OS statistics provider");
        final var kafkaTopicName = configuration.getString("os.collector.topicname");
        final var statisticsProducer = ProducerFactory.createProducer(configuration);
        if (kafkaTopicName.isBlank() || kafkaTopicName.isEmpty())
            throw new IllegalArgumentException("Kafka topic name has not been set");
        final var host = HostFactory.createHost(configuration);
        return new OsStatisticsProvider(host, kafkaTopicName, statisticsProducer).onExitShutdown();
    }

    public void scheduleEach(long seconds) {
        LOGGER.info("Start scheduler for the host: {} with ip address: {} and for each: {} seconds",
                host.getHostname(), host.getIpAddress(), seconds);
        scheduledExecutorService.scheduleAtFixedRate(
                this::collectHostInformationAndSendMessage,
                INITIAL_DELAY_SECONDS,
                seconds,
                TimeUnit.SECONDS
        );
    }

    protected void collectHostInformationAndSendMessage() {
        try {
            statisticsProducer.send(
                    new ProducerRecord<>(
                            kafkaTopicName,
                            UUID.randomUUID(),
                            host.copy(hardwareStatisticsCollector.loadHardwareInfo(), fileSystemStatisticsCollector.loadFileSystemInfo())
                    ),
                    (recordMetadata, e) -> {
                        if (isNull(e))
                            Utils.executeIfNotNull(applicationHealth, applicationHealth::incrementSuccessfulSent);
                        else if (isNonRepeatableException(e)) {
                            LOGGER.error("Couldn't send message: {}", recordMetadata);
                            Utils.executeIfNotNull(applicationHealth, applicationHealth::incrementFailedToSend);
                        }
                    }
            );
        } catch (Exception e) {
            LOGGER.error("Couldn't process data to Kafka", e);
            Thread.currentThread().interrupt();//stop processing
        }
    }

    private boolean isNonRepeatableException(Exception e) {
        return e instanceof InvalidTopicException ||
               e instanceof RecordBatchTooLargeException ||
               e instanceof UnknownServerException ||
               e instanceof OffsetMetadataTooLarge;
    }

    public OsStatisticsProvider withHealth(StatisticsProducerApplicationHealth applicationHealth) {
        this.applicationHealth = applicationHealth;
        return this;
    }

    public OsStatisticsProvider onExitShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        Utils.shutdownHukFor(scheduledExecutorService);
        return this;
    }

    public void shutdown() {
        if (nonNull(statisticsProducer))
            statisticsProducer.close();
    }

}
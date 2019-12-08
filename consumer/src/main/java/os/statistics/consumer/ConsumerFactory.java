package os.statistics.consumer;

import com.typesafe.config.Config;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import os.statistics.commons.model.Host;
import os.statistics.model.serialization.HostJsonDeserializer;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

public final class ConsumerFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConsumerFactory.class);

    public static Consumer<UUID, Host> createConsumer(Config configuration) {
        final var kafkaProperties = new Properties();

        kafkaProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getString(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        kafkaProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, configuration.getString(ConsumerConfig.GROUP_ID_CONFIG));
        kafkaProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, configuration.getString(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG));
        if (configuration.hasPath(ConsumerConfig.FETCH_MIN_BYTES_CONFIG))
            kafkaProperties.setProperty(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, configuration.getString(ConsumerConfig.FETCH_MIN_BYTES_CONFIG));
        if (configuration.hasPath(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG))
            kafkaProperties.setProperty(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, configuration.getString(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG));
        if (configuration.hasPath(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG))
            kafkaProperties.setProperty(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, configuration.getString(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG));
        if (configuration.hasPath(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG))
            kafkaProperties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, configuration.getString(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG));

        kafkaProperties.setProperty("security.protocol", configuration.getString("security.protocol"));
        kafkaProperties.setProperty("ssl.endpoint.identification.algorithm", configuration.getString("ssl.endpoint.identification.algorithm"));
        kafkaProperties.setProperty("ssl.truststore.location", configuration.getString("ssl.truststore.location"));
        kafkaProperties.setProperty("ssl.truststore.password", configuration.getString("ssl.truststore.password"));
        kafkaProperties.setProperty("ssl.keystore.type", configuration.getString("ssl.keystore.type"));
        kafkaProperties.setProperty("ssl.keystore.location", configuration.getString("ssl.keystore.location"));
        kafkaProperties.setProperty("ssl.keystore.password", configuration.getString("ssl.keystore.password"));
        kafkaProperties.setProperty("ssl.key.password", configuration.getString("ssl.key.password"));

        LOGGER.info("Create consumer with properties: {}", kafkaProperties);
        final var consumer = new KafkaConsumer<>(kafkaProperties, new UUIDDeserializer(), new HostJsonDeserializer());
        consumer.subscribe(List.of(configuration.getString("consumer.topicname")));
        return consumer;
    }

}

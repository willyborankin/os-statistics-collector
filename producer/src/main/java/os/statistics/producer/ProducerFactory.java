package os.statistics.producer;

import com.typesafe.config.Config;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import os.statistics.commons.model.Host;
import os.statistics.model.serialization.HostJsonSerializer;
import os.statistics.producer.health.StatisticsProducerApplicationHealth;

import java.util.Properties;
import java.util.UUID;

public final class ProducerFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProducerFactory.class);

    public static Producer<UUID, Host> createProducer(Config configuration) {
        final var kafkaProperties = new Properties();
        kafkaProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getString(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        kafkaProperties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, String.format("%s-%s", StatisticsProducerApplicationHealth.APPLICATION_NAME, UUID.randomUUID().toString()));
        if (configuration.hasPath(ProducerConfig.BUFFER_MEMORY_CONFIG))
            kafkaProperties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, configuration.getString(ProducerConfig.BUFFER_MEMORY_CONFIG));
        if (configuration.hasPath(ProducerConfig.COMPRESSION_TYPE_CONFIG))
            kafkaProperties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, configuration.getString(ProducerConfig.COMPRESSION_TYPE_CONFIG));
        if (configuration.hasPath(ProducerConfig.RETRIES_CONFIG))
            kafkaProperties.setProperty(ProducerConfig.RETRIES_CONFIG, configuration.getString(ProducerConfig.RETRIES_CONFIG));
        if (configuration.hasPath(ProducerConfig.ACKS_CONFIG))
            kafkaProperties.setProperty(ProducerConfig.ACKS_CONFIG, configuration.getString(ProducerConfig.ACKS_CONFIG));

        kafkaProperties.setProperty("security.protocol", configuration.getString("security.protocol"));
        kafkaProperties.setProperty("ssl.endpoint.identification.algorithm", configuration.getString("ssl.endpoint.identification.algorithm"));
        kafkaProperties.setProperty("ssl.truststore.location", configuration.getString("ssl.truststore.location"));
        kafkaProperties.setProperty("ssl.truststore.password", configuration.getString("ssl.truststore.password"));
        kafkaProperties.setProperty("ssl.keystore.type", configuration.getString("ssl.keystore.type"));
        kafkaProperties.setProperty("ssl.keystore.location", configuration.getString("ssl.keystore.location"));
        kafkaProperties.setProperty("ssl.keystore.password", configuration.getString("ssl.keystore.password"));
        kafkaProperties.setProperty("ssl.key.password", configuration.getString("ssl.key.password"));
        LOGGER.info("Create producer with properties: {}", kafkaProperties);
        return new KafkaProducer<>(kafkaProperties, new UUIDSerializer(), new HostJsonSerializer());
    }

}

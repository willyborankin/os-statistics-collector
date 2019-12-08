package os.statistics.producer;

import com.typesafe.config.Config;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import os.statistics.commons.model.Host;
import os.statistics.model.serialization.HostJsonSerializer;
import os.statistics.producer.health.StatisticsProducerApplicationHealth;

import java.util.Properties;
import java.util.UUID;

public final class ProducerFactory {

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

        return new KafkaProducer<>(kafkaProperties, new UUIDSerializer(), new HostJsonSerializer());
    }

}

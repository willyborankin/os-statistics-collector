package os.statistics.producer.statistics;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.RecordBatchTooLargeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import os.statistics.commons.model.Host;
import os.statistics.producer.health.StatisticsProducerApplicationHealth;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OsStatisticsProviderTest {

    @Mock
    private Producer<UUID, Host> mockedKafkaProducer;

    @Mock
    private StatisticsProducerApplicationHealth mockedApplicationHealth;

    @Captor
    private ArgumentCaptor<ProducerRecord<UUID, Host>> kafkaMessageCaptor;

    @Test
    public void throwsNullPointerExceptionForEmptyConfiguration() {
        assertThrows(
                NullPointerException.class,
                () -> OsStatisticsProvider.from(null)
        );
    }

    @Test
    public void collectStatisticsAndSendMessageToKafka() {
        new OsStatisticsProvider(
                new Host("some_host", "ip_address"),
                "some_topic",
                mockedKafkaProducer)
                .collectHostInformationAndSendMessage();

        verify(mockedKafkaProducer).send(kafkaMessageCaptor.capture(), any(Callback.class));

        final var producerRecord = kafkaMessageCaptor.getValue();
        assertEquals("some_topic", producerRecord.topic());

        assertNotNull(producerRecord.value().getFileSystem());
        assertNotNull(producerRecord.value().getHardware());
    }

    @Test
    public void collectorSendApplicationHealthInfo() {
        doAnswer((Answer<Void>) invocation -> {
            final var callback = (Callback) invocation.getArgument(1);
            callback.onCompletion(null, new RecordBatchTooLargeException());
            return null;
        }).when(mockedKafkaProducer).send(any(), any(Callback.class));

        new OsStatisticsProvider(
                new Host("some_host", "ip_address"),
                "some_topic",
                mockedKafkaProducer)
                .withHealth(mockedApplicationHealth)
                .collectHostInformationAndSendMessage();


        verify(mockedApplicationHealth).incrementFailedToSend();

    }
}
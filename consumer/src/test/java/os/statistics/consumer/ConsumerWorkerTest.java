package os.statistics.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import os.statistics.commons.model.FileSystem;
import os.statistics.commons.model.Hardware;
import os.statistics.commons.model.Host;
import os.statistics.consumer.db.resources.OsStatisticsDatabaseResource;
import os.statistics.consumer.health.StatisticsConsumerApplicationHealth;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsumerWorkerTest {

    @Mock
    private Consumer<UUID, Host> mockedConsumer;

    @Mock
    private OsStatisticsDatabaseResource mockedOsStatisticsDatabaseResource;

    @Mock
    private StatisticsConsumerApplicationHealth applicationHealth;

    @Test
    public void shouldConsumeAndStoreMessages() {
        when(mockedConsumer.poll(any(Duration.class))).thenReturn(createConsumerRecords());

        final var consumerWorker =
                new ConsumerWorker(mockedConsumer, mockedOsStatisticsDatabaseResource, Duration.ofMillis(100));
        consumerWorker.stopProcessing();
        consumerWorker.run();

        verify(mockedConsumer).poll(eq(Duration.ofMillis(100L)));
        verify(mockedOsStatisticsDatabaseResource, times(2))
                .addStatistics(any(Host.class));
        verify(mockedConsumer).commitSync();
        verify(mockedConsumer).close();
    }

    @Test
    public void shouldStopProcessingOnException() {
        when(mockedConsumer.poll(any(Duration.class))).thenThrow(RuntimeException.class);
        new ConsumerWorker(mockedConsumer, mockedOsStatisticsDatabaseResource, Duration.ofMillis(100))
                .run();

        verify(mockedConsumer).poll(eq(Duration.ofMillis(100L)));
        verify(mockedOsStatisticsDatabaseResource, never()).addStatistics(any(Host.class));
        verify(mockedConsumer, never()).commitSync();
        verify(mockedConsumer).close();
    }

    @Test
    public void shouldProviderApplicationHealthInfo() {
        when(mockedConsumer.poll(any(Duration.class))).thenReturn(createConsumerRecords());

        final var consumerWorker =
                new ConsumerWorker(mockedConsumer, mockedOsStatisticsDatabaseResource, Duration.ofMillis(100))
                        .withApplicationHealth(applicationHealth);
        consumerWorker.stopProcessing();
        consumerWorker.run();

        verify(applicationHealth).incrementConsumedMessages(2);

        verify(mockedConsumer).poll(eq(Duration.ofMillis(100L)));
        verify(mockedOsStatisticsDatabaseResource, times(2))
                .addStatistics(any(Host.class));
        verify(mockedConsumer).commitSync();
        verify(mockedConsumer).close();
    }

    private ConsumerRecords<UUID, Host> createConsumerRecords() {
        return new ConsumerRecords<>(
                        Map.of(new TopicPartition("some_topic", 1),
                                List.of(consumerRecord(), consumerRecord())));
    }

    private ConsumerRecord<UUID, Host> consumerRecord() {
        return new ConsumerRecord<>("some_tpoic", 1, 100L, UUID.randomUUID(), createHost());
    }

    private Host createHost() {
        return new Host(
                "some_host",
                "0.0.0.0")
                .copy(new Hardware(1L, 2L, 3L, 4L, 5L, 6L),
                      List.of(new FileSystem("/", 7L, 8L, 9L)));
    }

}
package os.statistics.consumer;

import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import os.statistics.commons.utils.Utils;
import os.statistics.consumer.health.StatisticsConsumerApplicationHealth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class ConsumerProvider {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConsumerProvider.class);

    private final ExecutorService executorService;

    private ConsumerProvider(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static ConsumerProvider from(Config configuration, StatisticsConsumerApplicationHealth applicationHealth) {
        final var threadsAmount = configuration.getInt("consumer.threads.amount");
        final var executorService = Executors.newFixedThreadPool(threadsAmount);
        IntStream.range(0, threadsAmount).forEach(i -> {
            LOGGER.info("Create consumer worker #{}", i);
            executorService.submit(ConsumerWorker.from(configuration).withApplicationHealth(applicationHealth));
        });
        return new ConsumerProvider(executorService).onExitShutdown();
    }

    public ConsumerProvider onExitShutdown() {
        Utils.shutdownHukFor(executorService);
        return this;
    }

}

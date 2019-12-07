package os.statistics.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import os.statistics.commons.health.ApplicationHealthMBeanRegistrator;
import os.statistics.commons.utils.Utils;
import os.statistics.producer.health.StatisticsProducerApplicationHealth;
import os.statistics.producer.health.StatisticsProducerApplicationHealthMBean;
import os.statistics.producer.statistics.OsStatisticsProvider;

import java.util.concurrent.TimeUnit;

public class StatisticsProducerApplicationLauncher {

    private final static Logger LOGGER = LoggerFactory.getLogger(StatisticsProducerApplicationLauncher.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("Start OS statistics producer");
        final var applicationHealth = new StatisticsProducerApplicationHealth();
        ApplicationHealthMBeanRegistrator.register(applicationHealth, StatisticsProducerApplicationHealthMBean.class);
        final var configuration = Utils.loadConfiguration("producer.conf");
        final var seconds = configuration.getDuration("os.collector.tick.duration", TimeUnit.SECONDS);
        try {
            OsStatisticsProvider
                    .from(configuration)
                    .withHealth(applicationHealth)
                    .onExitShutdown()
                    .scheduleEach(seconds);
        } finally {
            ApplicationHealthMBeanRegistrator.unregister(applicationHealth.getApplicationName());
        }
    }

}

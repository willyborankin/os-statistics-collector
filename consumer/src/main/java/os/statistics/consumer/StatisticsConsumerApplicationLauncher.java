package os.statistics.consumer;

import com.typesafe.config.Config;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import os.statistics.commons.health.ApplicationHealthMBeanRegistrator;
import os.statistics.commons.utils.Utils;
import os.statistics.consumer.health.StatisticsConsumerApplicationHealth;
import os.statistics.consumer.health.StatisticsConsumerApplicationHealthMBean;

public class StatisticsConsumerApplicationLauncher {

    private final static Logger LOGGER = LoggerFactory.getLogger(StatisticsConsumerApplicationLauncher.class);

    public static void main(String[] args) {
        var configuration = Utils.loadConfiguration("consumer.conf");
        databaseMigration(configuration);
        final var applicationHealth = new StatisticsConsumerApplicationHealth();
        ApplicationHealthMBeanRegistrator.register(applicationHealth, StatisticsConsumerApplicationHealthMBean.class);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ApplicationHealthMBeanRegistrator.unregister(applicationHealth.getApplicationName());
        }));
        ConsumerProvider.from(configuration, applicationHealth);
    }

    private static void databaseMigration(Config configuration) {
        LOGGER.info("Begin flyway migration");
        var flyway =
                Flyway.configure()
                      .locations(configuration.getString("flyway.migration.locations"))
                      .dataSource(
                              configuration.getString("jdbc.db.url"),
                              configuration.getString("flyway.migration.user"),
                              configuration.getString("flyway.migration.password"))

                      .load();
        flyway.repair();
        flyway.migrate();
    }

}

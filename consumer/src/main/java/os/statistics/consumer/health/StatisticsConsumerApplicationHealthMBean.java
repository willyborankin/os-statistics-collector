package os.statistics.consumer.health;

import os.statistics.commons.health.ApplicationHealthMBean;

public interface StatisticsConsumerApplicationHealthMBean extends ApplicationHealthMBean {

    long getConsumedMessages();

}

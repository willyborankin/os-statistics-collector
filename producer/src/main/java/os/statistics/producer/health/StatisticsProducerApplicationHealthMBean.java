package os.statistics.producer.health;

import os.statistics.commons.health.ApplicationHealthMBean;

public interface StatisticsProducerApplicationHealthMBean extends ApplicationHealthMBean {

    long getSuccessfulSent();

    long getFailedToSend();

}

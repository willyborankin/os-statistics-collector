package os.statistics.commons.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;

public class ApplicationHealthMBeanRegistrator {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationHealthMBeanRegistrator.class);

    private final static String MBEAN_NAME_PATTERN = "os.statistics.commons.health:type=APPLICATION-%s-%s";

    public static <T extends ApplicationHealthMBean> void register(T applicationHealthMBean, Class<T> mbeanClass) {
        try {
            final var mbeanServer = ManagementFactory.getPlatformMBeanServer();
            final var mBeanName = String.format(MBEAN_NAME_PATTERN, applicationHealthMBean.getApplicationName(), ProcessHandle.current().pid());
            LOGGER.info("Register Health application MBean with name: {}", mBeanName);
            mbeanServer.registerMBean(new StandardMBean(applicationHealthMBean, mbeanClass), new ObjectName(mBeanName));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Couldn't register ApplicationStatusMBean for application %s", "applicationHealthMBean.getApplicationName()"), e);
        }
    }

    public static void unregister(String applicationName) {
        try {
            final var mbeanServer = ManagementFactory.getPlatformMBeanServer();
            mbeanServer.unregisterMBean(new ObjectName(String.format(MBEAN_NAME_PATTERN, applicationName, ProcessHandle.current().pid())));
        } catch (Exception e) {
            throw new RuntimeException("Couldn't register Application status MBean", e);
        }
    }

}

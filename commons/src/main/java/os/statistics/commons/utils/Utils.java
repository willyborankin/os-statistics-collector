package os.statistics.commons.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Objects;

public class Utils {

    private final static Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    @FunctionalInterface
    public interface NonNullCallback<T> {

        void call();

    }

    public static Config loadConfiguration(String fileName) {
        LOGGER.info("Load default configuration");
        return ConfigFactory
                .parseFile(Paths.get(fileName).toFile())
                .withFallback(ConfigFactory.defaultReference());

    }

    public static <T> void executeIfNotNull(T objectToCheck, NonNullCallback<T> callback) {
        if (Objects.nonNull(objectToCheck))
            callback.call();
    }

}

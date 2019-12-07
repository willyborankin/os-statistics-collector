package os.statistics.model.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import os.statistics.commons.model.Host;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class HostJsonDeserializer implements Deserializer<Host> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Host deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Host.class);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't deserialize message", e);
        }
    }

}

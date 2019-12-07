package os.statistics.model.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import os.statistics.commons.model.Host;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Objects;

public class HostJsonSerializer implements Serializer<Host> {

    private final ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Host data) {
        Objects.requireNonNull(data, "Host has not been set");
        try {
            return jsonObjectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Couldn't serialize object int json", e);
        }
    }

}

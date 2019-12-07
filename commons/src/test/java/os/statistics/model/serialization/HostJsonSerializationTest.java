package os.statistics.model.serialization;

import os.statistics.commons.model.FileSystem;
import os.statistics.commons.model.Hardware;
import os.statistics.commons.model.Host;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HostJsonSerializationTest {

    private final HostJsonSerializer hostJsonSerializer = new HostJsonSerializer();

    private final HostJsonDeserializer hostJsonDeserializer = new HostJsonDeserializer();

    @Test
    public void throwsRuntimeExceptionForEmptyMessage() {
        assertThrows(NullPointerException.class, () -> hostJsonSerializer.serialize("some_topic", null));
    }

    @Test
    public void canSerializeDeserializeObjectWithOutHardwareInfo() throws Exception {
        final var host = new Host("some_host", "som_ip_addr");
        final var serializedValue = hostJsonSerializer.serialize("some_topic", host);
        final var hostFromBytes = hostJsonDeserializer.deserialize("some_topic", serializedValue);

        assertEquals(hostFromBytes, host);
    }

    @Test
    public void canSerializeDeserializeFullObject() {
        final var host =
                new Host("some_host", "som_ip_addr")
                        .copy(
                                new Hardware(1, 2, 3, 4, 5, 6),
                                List.of(new FileSystem("/", 1, 2, 3))
                        );
        final var serializedValue = hostJsonSerializer.serialize("some_topic", host);
        final var hostFromBytes = hostJsonDeserializer.deserialize("some_topic", serializedValue);

        assertEquals(hostFromBytes, host);
    }

}
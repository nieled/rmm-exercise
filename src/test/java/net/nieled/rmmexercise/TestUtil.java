package net.nieled.rmmexercise;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtil {

    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public static <T> void equalsVerifier(Class<T> pClass) throws Exception {
        T obj1 = pClass.getConstructor().newInstance();
        assertThat(obj1.toString()).isNotNull();
        assertThat(obj1).isEqualTo(obj1);
        assertThat(obj1).hasSameHashCodeAs(obj1);
        // Test with an instance of another class
        Object testOtherObject = new Object();
        assertThat(obj1).isNotEqualTo(testOtherObject);
        assertThat(obj1).isNotEqualTo(null);
        // Test with an instance of the same class
        T obj2 = pClass.getConstructor().newInstance();
        assertThat(obj1).isNotEqualTo(obj2);
        // HashCodes are equals because the objects are not persisted yet
        assertThat(obj1).hasSameHashCodeAs(obj2);
    }

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert.
     * @return the JSON byte array.
     * @throws IOException IO error when converting JSON Bytes
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

}

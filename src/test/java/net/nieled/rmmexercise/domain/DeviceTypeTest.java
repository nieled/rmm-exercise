package net.nieled.rmmexercise.domain;

import net.nieled.rmmexercise.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceType.class);
        DeviceType deviceType1 = new DeviceType();
        deviceType1.setId(1L);
        DeviceType deviceType2 = new DeviceType();
        deviceType2.setId(deviceType1.getId());
        assertThat(deviceType1).isEqualTo(deviceType2);
        deviceType2.setId(2L);
        assertThat(deviceType1).isNotEqualTo(deviceType2);
        deviceType1.setId(null);
        assertThat(deviceType1).isNotEqualTo(deviceType2);
    }

}

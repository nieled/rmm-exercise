package net.nieled.rmmexercise.domain;

import net.nieled.rmmexercise.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Service.class);
        Service service1 = new Service();
        service1.setId(1L);
        Service service2 = new Service();
        service2.setId(service1.getId());
        assertThat(service1).isEqualTo(service2);
        service2.setId(2L);
        assertThat(service1).isNotEqualTo(service2);
        service1.setId(null);
        assertThat(service1).isNotEqualTo(service2);
    }

}
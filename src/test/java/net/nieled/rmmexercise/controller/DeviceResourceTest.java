package net.nieled.rmmexercise.controller;

import net.nieled.rmmexercise.IntegrationTest;
import net.nieled.rmmexercise.TestUtil;
import net.nieled.rmmexercise.domain.Device;
import net.nieled.rmmexercise.domain.Service;
import net.nieled.rmmexercise.domain.enums.OS;
import net.nieled.rmmexercise.repository.DeviceRepository;
import net.nieled.rmmexercise.repository.ServiceRepository;
import net.nieled.rmmexercise.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class DeviceResourceTest {

    private static final String DEFAULT_SYSTEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SYSTEM_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();

    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    public static Device createEntity() {
        return new Device(null, DEFAULT_SYSTEM_NAME, null, new HashSet<>(), null);
    }

    @BeforeEach
    public void initTest() {
        device = createEntity();
    }

    @Test
    @Transactional
    void createDevice() throws Exception {
        var databaseSizeBeforeCreate = deviceRepository.findAll().size();
        restDeviceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
                .andExpect(status().isCreated());

        var deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        var testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getSystemName()).isEqualTo(DEFAULT_SYSTEM_NAME);
    }

    @Test
    @Transactional
    void createDeviceWithExistingId() throws Exception {
        device.setId(1L);
        var databaseSizeBeforeCreate = deviceRepository.findAll().size();
        restDeviceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
                .andExpect(status().isBadRequest());

        var deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSystemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        device.setSystemName(null);
        restDeviceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
                .andExpect(status().isBadRequest());

        var deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        deviceRepository.saveAndFlush(device);
        restDeviceMockMvc
                .perform(get(ENTITY_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
                .andExpect(jsonPath("$.[*].systemName").value(hasItem(DEFAULT_SYSTEM_NAME)));
    }

    @Test
    @Transactional
    void getDevice() throws Exception {
        deviceRepository.saveAndFlush(device);
        restDeviceMockMvc
                .perform(get(ENTITY_API_URL_ID, device.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(device.getId().intValue()))
                .andExpect(jsonPath("$.systemName").value(DEFAULT_SYSTEM_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDevice() throws Exception {
        restDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDevice() throws Exception {
        deviceRepository.saveAndFlush(device);
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        var updatedDevice = deviceRepository.findById(device.getId()).get();
        em.detach(updatedDevice);
        updatedDevice.setSystemName(UPDATED_SYSTEM_NAME);
        restDeviceMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, updatedDevice.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(updatedDevice))
                )
                .andExpect(status().isOk());

        var deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        var testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getSystemName()).isEqualTo(UPDATED_SYSTEM_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDevice() throws Exception {
        var databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());
        restDeviceMockMvc
                .perform(
                        put(ENTITY_API_URL_ID, device.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(device))
                )
                .andExpect(status().isBadRequest());
        var deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevice() throws Exception {
        var databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());
        restDeviceMockMvc
                .perform(
                        patch(ENTITY_API_URL_ID, count.incrementAndGet())
                                .contentType("application/merge-patch+json")
                                .content(TestUtil.convertObjectToJsonBytes(device))
                )
                .andExpect(status().isBadRequest());

        var deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevice() throws Exception {
        var databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());
        restDeviceMockMvc
                .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(device)))
                .andExpect(status().isMethodNotAllowed());

        var deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDevice() throws Exception {
        deviceRepository.saveAndFlush(device);
        var databaseSizeBeforeDelete = deviceRepository.findAll().size();
        restDeviceMockMvc
                .perform(delete(ENTITY_API_URL_ID, device.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        var deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    void getCostOfServices() throws Exception {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("user@email.com", "password"));
        SecurityContextHolder.setContext(securityContext);
        var newUser = userRepository.findByEmail("user@email.com").get();

        var serviceA = new Service(null, "SERVICE_A", new BigDecimal(1), OS.WINDOWS);
        var serviceB = new Service(null, "SERVICE_B", new BigDecimal(2), OS.MACOS);
        var serviceC = new Service(null, "SERVICE_C", new BigDecimal(3), null);
        var servicesDeviceA = serviceRepository.saveAll(Arrays.asList(serviceA, serviceB));
        var servicesDeviceB = serviceRepository.saveAll(Arrays.asList(serviceA, serviceC));

        var deviceA = new Device(null, DEFAULT_SYSTEM_NAME, null, new HashSet<>(servicesDeviceA), newUser);
        var deviceB = new Device(null, DEFAULT_SYSTEM_NAME, null, new HashSet<>(servicesDeviceB), newUser);
        deviceRepository.saveAll(Arrays.asList(deviceA, deviceB));

        restDeviceMockMvc
                .perform(
                        get(ENTITY_API_URL + "/cost").with(user("user@email.com"))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("7"));
    }
}

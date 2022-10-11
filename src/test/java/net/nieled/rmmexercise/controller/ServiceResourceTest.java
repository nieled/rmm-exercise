package net.nieled.rmmexercise.controller;

import net.nieled.rmmexercise.IntegrationTest;
import net.nieled.rmmexercise.TestUtil;
import net.nieled.rmmexercise.domain.Service;
import net.nieled.rmmexercise.domain.enums.OS;
import net.nieled.rmmexercise.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceResourceTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final OS DEFAULT_OS = OS.WINDOWS;
    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private MockMvc restServiceMockMvc;

    private Service service;

    public static Service createEntity() {
        return new Service(null, DEFAULT_NAME, DEFAULT_COST, DEFAULT_OS);
    }

    @BeforeEach
    public void initTest() {
        service = createEntity();
    }

    @Test
    @Transactional
    void createService() throws Exception {
        var databaseSizeBeforeCreate = serviceRepository.findAll().size();
        restServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isCreated());

        var serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeCreate + 1);
        var testService = serviceList.get(serviceList.size() - 1);
        assertThat(testService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testService.getOs()).isEqualTo(DEFAULT_OS);
        assertThat(testService.getCost()).isEqualByComparingTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void createServiceWithExistingId() throws Exception {
        service.setId(1L);
        var databaseSizeBeforeCreate = serviceRepository.findAll().size();
        restServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isBadRequest());

        var serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = serviceRepository.findAll().size();
        service.setName(null);
        restServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isBadRequest());

        var serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        var databaseSizeBeforeTest = serviceRepository.findAll().size();
        service.setCost(null);
        restServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(service)))
                .andExpect(status().isBadRequest());

        var serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void deleteService() throws Exception {
        serviceRepository.saveAndFlush(service);
        var databaseSizeBeforeDelete = serviceRepository.findAll().size();
        restServiceMockMvc
                .perform(delete(ENTITY_API_URL_ID, service.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        var serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
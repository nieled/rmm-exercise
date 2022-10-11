package net.nieled.rmmexercise.controller;

import net.nieled.rmmexercise.IntegrationTest;
import net.nieled.rmmexercise.TestUtil;
import net.nieled.rmmexercise.controller.viewmodel.AuthRequest;
import net.nieled.rmmexercise.domain.User;
import net.nieled.rmmexercise.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@IntegrationTest
public class UserJWTControllerIT {

    private static final String VALID_EMAIL = "valid@email.com";
    private static final String UNRECOGNIZED_EMAIL = "not-registered@email.com";
    private static final String VALID_RAW_PASS = "password123";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void testLogin() throws Exception {
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(passwordEncoder.encode(VALID_RAW_PASS));

        userRepository.saveAndFlush(user);

        AuthRequest login = new AuthRequest();
        login.setEmail(VALID_EMAIL);
        login.setPassword(VALID_RAW_PASS);
        mockMvc
            .perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isString())
            .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void testFailedLogin() throws Exception {
        // No need to create a valid user

        AuthRequest login = new AuthRequest();
        login.setEmail(UNRECOGNIZED_EMAIL);
        login.setPassword(VALID_RAW_PASS);
        mockMvc
            .perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.accessToken").doesNotExist());
    }
}

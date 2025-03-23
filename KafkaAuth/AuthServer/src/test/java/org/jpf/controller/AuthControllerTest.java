package org.jpf.controller;

import org.jpf.model.dto.security.LoginRequest;
import org.jpf.model.dto.security.RegistrationRequest;
import org.jpf.model.kafka.ConfirmCodeEvent;
import org.jpf.model.security.RefreshToken;
import org.jpf.service.impl.UserServiceImpl;
import org.jpf.service.impl.kafka.RegistrationEventServiceImpl;
import org.jpf.service.impl.security.RefreshTokenServiceImpl;
import org.jpf.service.security.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("AuthControllerTest tests")
class AuthControllerTest {
    private final static String urlTemplate = "/api/auth";
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    RegistrationEventServiceImpl registrationEventService;
    @MockitoSpyBean
    RefreshTokenServiceImpl refreshTokenService;
    @MockitoSpyBean
    SecurityService securityService;
    @MockitoSpyBean
    UserServiceImpl userService;
    @MockitoSpyBean
    PasswordEncoder passwordEncoder;

    @Test
    @WithAnonymousUser
    @DisplayName("authUser test: auth user from anonymous user.")
    void givenLoginRequestWhenSigninUrlThenAuthResponse() throws Exception {
        String url = urlTemplate + "/signin";
        String email = "string@email.com";
        String pass = "pass";
        String encodedPass = passwordEncoder.encode(pass);
        String requestJson = """
                {
                   "email": "string@email.com",
                   "confirmCode":"pass"
                }
                """;
        LoginRequest loginRequest = new LoginRequest(
                "string@email.com", "pass"
        );
        userService.save(new ConfirmCodeEvent(email, encodedPass));

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andExpect(status().isOk());

        verify(securityService, times(1))
                .authenticationUser(loginRequest);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("registerUser test: new admin user from anonymous user.")
    void givenUserRequestWhenRegisterUrlThenMessage() throws Exception {
        String url = urlTemplate + "/register";
        String requestJson = """
                {
                "email": "string@email.com"
                }""";
        String responseJson = """
                {
                "message": "Check confirm code in string@email.com."
                }""";
        RegistrationRequest request = new RegistrationRequest("string@email.com");

        doNothing().when(registrationEventService).send(request);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(content().json(responseJson))
                .andExpect(status().isOk());

        verify(securityService, times(1))
                .registerNewUser(request);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("refreshToken test: refresh auth token from anonymous user.")
    void givenRefreshTokenRequestWhenRefreshTokenThenRefreshTokenResponse()
            throws Exception {
        String url = urlTemplate + "/refresh-token";
        String requestJson = """
                {
                   "refreshToken": "refreshToken"
                }""";
        RefreshToken refreshToken = new RefreshToken(
                "some@email.com",
                "updatedRefreshToken",
                Instant.now().plusSeconds(10)
        );

        when(refreshTokenService.findByRefreshToken("refreshToken"))
                .thenReturn(Optional.of(refreshToken));

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(jsonPath("$.refreshToken").isString())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(status().isOk());

        verify(securityService, times(1))
                .refreshToken(any());
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("logoutUser test: logout user from simple user.")
    void givenUserDetailsFromUserWhenLogoutUrlThenSimpleResponse()
            throws Exception {
        String url = urlTemplate + "/logout";

        mockMvc.perform(post(url))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(status().isOk());

        verify(securityService, times(1))
                .logout();
    }

    @Test
    @WithAnonymousUser
    @DisplayName("logoutUser test: logout user from anonymous user.")
    void givenUserDetailsFromAnonymousUserWhenLogoutUrlThenStatusForbidden()
            throws Exception {
        String url = urlTemplate + "/logout";

        mockMvc.perform(post(url))
                .andExpect(status().isForbidden());

        verify(securityService, times(0))
                .logout();
    }
}

package org.jpf.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("AppControllerTest tests")
class AppControllerTest {
    private static final String URL_TEMPLATE = "/api/test";
    private static final String PUBLIC_RESPONSE_MESSAGE = "Public response data.";
    private static final String ADMIN_RESPONSE_MESSAGE = "Admin response data.";
    private static final String USER_RESPONSE_MESSAGE = "User response data.";
    private static final String ANY_RESPONSE_MESSAGE = "Authenticated response data.";
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testAdmin", roles = {"ADMIN"})
    @DisplayName("allAccess test with admin user.")
    void givenAdminUser_whenAllAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/all"))
                .andExpect(content().string(PUBLIC_RESPONSE_MESSAGE));
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("allAccess test with simple user.")
    void givenSimpleUser_whenAllAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/all"))
                .andExpect(content().string(PUBLIC_RESPONSE_MESSAGE));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("allAccess test with anonymous user.")
    void givenAnonymousUser_whenAllAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/all"))
                .andExpect(content().string(PUBLIC_RESPONSE_MESSAGE));
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = {"ADMIN"})
    @DisplayName("adminAccess test with admin user.")
    void givenAdminUser_whenAdminAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/admin"))
                .andExpect(content().string(ADMIN_RESPONSE_MESSAGE));
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("adminAccess test with simple user.")
    void givenSimpleUser_whenAdminAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("adminAccess test with anonymous user.")
    void givenAnonymousUser_whenAdminAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = {"ADMIN"})
    @DisplayName("userAccess test with admin user.")
    void givenAdminUser_whenUserAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("anyAccess test with simple user.")
    void givenSimpleUser_whenUserAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/user"))
                .andExpect(content().string(USER_RESPONSE_MESSAGE));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("userAccess test with anonymous user.")
    void givenAnonymousUser_whenUserAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", roles = {"ADMIN"})
    @DisplayName("anyAccess test with admin user.")
    void givenAdminUser_whenAnyAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/any"))
                .andExpect(content().string(ANY_RESPONSE_MESSAGE));
    }

    @Test
    @WithMockUser(username = "testUser")
    @DisplayName("anyAccess test with simple user.")
    void givenSimpleUser_whenAnyAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/any"))
                .andExpect(content().string(ANY_RESPONSE_MESSAGE));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("anyAccess test with anonymous user.")
    void givenAnonymousUser_whenAnyAccessUrl_thenMessage() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE + "/any"))
                .andExpect(status().isForbidden());
    }
}

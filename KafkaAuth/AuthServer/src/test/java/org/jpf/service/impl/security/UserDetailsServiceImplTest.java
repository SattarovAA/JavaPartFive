package org.jpf.service.impl.security;

import org.jpf.model.entity.User;
import org.jpf.model.security.AppUserDetails;
import org.jpf.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsServiceImplTest Tests")
class UserDetailsServiceImplTest {
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private UserService userService;

    @Test
    @DisplayName("loadUserByUsername test: try to load " +
                 "user by username with correct username.")
    void givenExistingUsernameWhenLoadUserByUsernameThenUserDetails() {
        String userEmail = "string@mail.com";
        User existingUser = new User(
                "pass",
                userEmail
        );
        UserDetails expected = new AppUserDetails(existingUser);

        when(userService.findByEmail(userEmail))
                .thenReturn(existingUser);

        UserDetails actual = userDetailsService.loadUserByUsername(userEmail);

        assertEquals(expected, actual);
        verify(userService, times(1))
                .findByEmail(userEmail);
    }
}

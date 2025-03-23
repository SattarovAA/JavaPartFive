package org.jpf.service.impl.security;

import org.jpf.exception.security.RefreshTokenException;
import org.jpf.jwt.JwtUtils;
import org.jpf.model.dto.security.AuthResponse;
import org.jpf.model.dto.security.LoginRequest;
import org.jpf.model.dto.security.RefreshTokenRequest;
import org.jpf.model.dto.security.RefreshTokenResponse;
import org.jpf.model.dto.security.RegistrationRequest;
import org.jpf.model.entity.User;
import org.jpf.model.security.AppUserDetails;
import org.jpf.model.security.RefreshToken;
import org.jpf.service.impl.kafka.RegistrationEventServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityServiceImplTest tests")
class SecurityServiceImplTest {
    @InjectMocks
    private SecurityServiceImpl securityService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private RefreshTokenServiceImpl refreshTokenService;
    @Mock
    private RegistrationEventServiceImpl registrationEventService;

    @Test
    @DisplayName("authenticationUser test: try to authenticate " +
                 "user with LoginRequest.")
    void givenCorrectLoginRequestWhenAuthenticationThenAuthResponse() {
        String userEmail = "string@email.com";
        String confirmCode = "pass";
        String jwtToken = "jwtToken";
        User defaultUser = new User(
                confirmCode, userEmail
        );
        AppUserDetails principal = new AppUserDetails(defaultUser);
        LoginRequest loginRequest = new LoginRequest(
                userEmail, confirmCode
        );
        UsernamePasswordAuthenticationToken expectedToken =
                new UsernamePasswordAuthenticationToken(
                        userEmail, loginRequest.confirmCode()
                );
        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities()
                );
        RefreshToken refreshToken = new RefreshToken(
                userEmail, "token", Instant.now()
        );
        AuthResponse expected = AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.token())
                .build();

        when(refreshTokenService.createRefreshToken(userEmail))
                .thenReturn(refreshToken);
        when(jwtUtils.generateTokenFromEmail(userEmail))
                .thenReturn(jwtToken);
        when(authenticationManager.authenticate(expectedToken))
                .thenReturn(auth);
        AuthResponse actual = securityService.authenticationUser(loginRequest);

        assertEquals(expected, actual);
        verify(authenticationManager, times(1))
                .authenticate(expectedToken);
        verify(refreshTokenService, times(1))
                .createRefreshToken(userEmail);
        verify(jwtUtils, times(1))
                .generateTokenFromEmail(userEmail);
    }

    @Test
    @DisplayName("registerNewUser test: send correct RegistrationEvent " +
                 "to registrationEventService.")
    void givenUserWhenRegisterNewUserThenUser() {
        var request = new RegistrationRequest("string@mail.com");

        securityService.registerNewUser(request);

        verify(registrationEventService, times(1))
                .send(request);
    }

    @Test
    @DisplayName("refreshToken test: update correct refreshToken.")
    void givenRefreshTokenRequestWhenRefreshTokenThenRefreshTokenResponse() {
        String userEmail = "string@email.com";
        String refreshTokenValue = "refreshTokenRequest";
        String updatedTokenValue = "updatedRefreshToken";
        String jwtToken = "jwtToken";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(
                refreshTokenValue
        );
        RefreshToken oldRefreshToken = new RefreshToken(
                userEmail, "oldRefreshToken", Instant.now()
        );
        RefreshToken updatedRefreshToken = new RefreshToken(
                userEmail, updatedTokenValue, Instant.now()
        );

        when(refreshTokenService.findByRefreshToken(refreshTokenValue))
                .thenReturn(Optional.of(oldRefreshToken));
        when(refreshTokenService.checkRefreshToken(oldRefreshToken))
                .thenReturn(oldRefreshToken);
        when(jwtUtils.generateTokenFromEmail(userEmail))
                .thenReturn(jwtToken);
        when(refreshTokenService.createRefreshToken(userEmail))
                .thenReturn(updatedRefreshToken);

        RefreshTokenResponse expected = new RefreshTokenResponse(
                updatedTokenValue,
                jwtToken
        );
        RefreshTokenResponse actual =
                securityService.refreshToken(refreshTokenRequest);

        assertEquals(expected, actual);
        verify(refreshTokenService, times(1))
                .findByRefreshToken(refreshTokenValue);
        verify(refreshTokenService, times(1))
                .checkRefreshToken(oldRefreshToken);
        verify(jwtUtils, times(1))
                .generateTokenFromEmail(userEmail);
        verify(refreshTokenService, times(1))
                .createRefreshToken(userEmail);
    }

    @Test
    @DisplayName("refreshToken test: throw when refreshToken not found.")
    void givenRefreshTokenRequestWhenRefreshTokenThenRefreshThrow() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(
                "refreshTokenRequest"
        );

        when(refreshTokenService.findByRefreshToken("refreshTokenRequest"))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenException.class,
                () -> securityService.refreshToken(refreshTokenRequest),
                "refreshToken not found."
        );
        verify(refreshTokenService, times(1))
                .findByRefreshToken(any());
        verify(refreshTokenService, times(0))
                .checkRefreshToken(any());
        verify(jwtUtils, times(0))
                .generateTokenFromEmail(any());
        verify(refreshTokenService, times(0))
                .createRefreshToken(any());
    }

    @Test
    @DisplayName("logout test: delete correct refreshToken by userEmail.")
    void givenCorrectPrincipalWhenLogoutThenCallDeleteRefreshToken() {
        String userEmail = "string@email.com";
        AppUserDetails principal =
                new AppUserDetails(new User(
                        "pass",
                        userEmail
                ));
        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(auth);

        securityService.logout();

        verify(refreshTokenService, times(1))
                .deleteByUserEmail(userEmail);
    }
}

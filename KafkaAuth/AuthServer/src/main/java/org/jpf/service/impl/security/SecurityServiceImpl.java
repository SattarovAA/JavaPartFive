package org.jpf.service.impl.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpf.exception.security.RefreshTokenException;
import org.jpf.jwt.JwtUtils;
import org.jpf.model.dto.security.AuthResponse;
import org.jpf.model.dto.security.LoginRequest;
import org.jpf.model.dto.security.RefreshTokenRequest;
import org.jpf.model.dto.security.RefreshTokenResponse;
import org.jpf.model.dto.security.RegistrationRequest;
import org.jpf.model.security.AppUserDetails;
import org.jpf.model.security.RefreshToken;
import org.jpf.service.kafka.RegistrationEventService;
import org.jpf.service.security.RefreshTokenService;
import org.jpf.service.security.SecurityService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityServiceImpl implements SecurityService {
    /**
     * Service for send user registration event.
     */
    private final RegistrationEventService registrationEventService;
    /**
     * Service for work with {@link RefreshToken} entity.
     */
    private final RefreshTokenService refreshTokenService;
    /**
     * To authentication user by confirmCode and username.
     *
     * @see #getAuthenticationFromLoginRequest(LoginRequest)
     */
    private final AuthenticationManager authenticationManager;
    /**
     * To generate jwt token.
     */
    private final JwtUtils jwtUtils;

    public AuthResponse authenticationUser(LoginRequest loginRequest) {
        Authentication authentication =
                getAuthenticationFromLoginRequest(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetails userDetails =
                (AppUserDetails) authentication.getPrincipal();
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(userDetails.getEmail());
        return AuthResponse.builder()
                .token(jwtUtils.generateTokenFromEmail(loginRequest.email()))
                .refreshToken(refreshToken.token())
                .build();
    }

    private Authentication getAuthenticationFromLoginRequest(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.confirmCode()
                )
        );
    }

    @Override
    public void registerNewUser(RegistrationRequest user) {
        registrationEventService.send(user);
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshTokenRequest = request.refreshToken();
        return refreshTokenService.findByRefreshToken(refreshTokenRequest)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::email)
                .map(email -> {
                    String token = jwtUtils.generateTokenFromEmail(email);
                    return new RefreshTokenResponse(
                            refreshTokenService.createRefreshToken(email).token(),
                            token
                    );
                }).orElseThrow(() -> new RefreshTokenException(refreshTokenRequest,
                        "refresh token not found"));
    }

    @Override
    public void logout() {
        var currentPrincipal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            String email = userDetails.getEmail();
            refreshTokenService.deleteByUserEmail(email);
        }
    }
}

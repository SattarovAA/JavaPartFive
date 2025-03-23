package org.jpf.service.security;

import org.jpf.model.dto.security.AuthResponse;
import org.jpf.model.dto.security.LoginRequest;
import org.jpf.model.dto.security.RefreshTokenRequest;
import org.jpf.model.dto.security.RefreshTokenResponse;
import org.jpf.model.dto.security.RegistrationRequest;
import org.jpf.model.entity.User;

/**
 * Service interface for work with new {@link User}.
 */
public interface SecurityService {
    /**
     * Authentication user in system by email and confirmCode.
     *
     * @param loginRequest {@link LoginRequest} to authenticate.
     * @return {@link AuthResponse} with Authentication information.
     */
    AuthResponse authenticationUser(LoginRequest loginRequest);

    /**
     * Registration new {@link User}.
     *
     * @param user {@link User} for registration.
     */
    void registerNewUser(RegistrationRequest user);

    /**
     * get {@link RefreshTokenResponse} with updated token
     * from {@link RefreshTokenResponse} with correct refreshToken.
     *
     * @param request {@link RefreshTokenResponse} with refreshToken.
     * @return {@link RefreshTokenResponse} with updated token.
     */
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    /**
     * Logout from system.
     * Delete token in system by user Id.
     */
    void logout();
}

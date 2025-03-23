package org.jpf.service.security;

import org.jpf.model.security.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    /**
     * Find {@link RefreshToken} by string value.
     *
     * @param token to search {@link RefreshToken}.
     * @return searhed {@link RefreshToken} if exists.
     */
    Optional<RefreshToken> findByRefreshToken(String token);

    /**
     * Create new {@link RefreshToken} by user email.
     *
     * @param email user to create token.
     * @return created {@link RefreshToken}.
     */
    RefreshToken createRefreshToken(String email);

    /**
     * Check {@link RefreshToken} to expiry.
     *
     * @param token token to check
     * @return correct {@link RefreshToken}.
     */
    RefreshToken checkRefreshToken(RefreshToken token);

    /**
     * Delete all tokens with userId.
     *
     * @param email user email to delete token.
     */
    void deleteByUserEmail(String email);

    /**
     * Check token by user id.
     *
     * @param email to check.
     * @return true if user with id exists.
     */
    boolean checkByEmail(String email);
}

package org.jpf.model.dto.security;

public record RefreshTokenResponse(
        String refreshToken,
        String accessToken
) {
}

package org.jpf.model.dto.security;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String refreshToken
) {
}

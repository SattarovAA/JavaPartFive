package org.jpf.model.security;

import lombok.Builder;

import java.time.Instant;

@Builder
public record RefreshToken(
        String email,
        String token,
        Instant expiryDate
) {
}
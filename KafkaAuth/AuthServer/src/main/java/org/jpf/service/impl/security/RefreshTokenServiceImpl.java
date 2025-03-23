package org.jpf.service.impl.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jpf.exception.security.RefreshTokenException;
import org.jpf.model.security.RefreshToken;
import org.jpf.service.security.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final Map<String, RefreshToken> repository;
    @Value("${app.security.jwt.refresh-token.expiration}")
    Duration refreshTokenExpiry;

    public Optional<RefreshToken> findByRefreshToken(String token) {
        return Optional.ofNullable(
                repository.get(token)
        );
    }

    public RefreshToken createRefreshToken(String email) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .email(email)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiry.toMillis()))
                .token(token)
                .build();
        log.info("Create new refreshToken: {}", refreshToken);
        repository.put(token, refreshToken);
        return refreshToken;
    }

    public RefreshToken checkRefreshToken(RefreshToken token) {
        if (token.expiryDate().compareTo(Instant.now()) < 0) {
            repository.remove(token.token());
            throw new RefreshTokenException(token.token(),
                    "Refresh token was expired. Repeat signin action!");
        }
        return token;
    }

    public void deleteByUserEmail(String email) {
        Iterator<Map.Entry<String, RefreshToken>> iterator = repository.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, RefreshToken> entry = iterator.next();
            if (entry.getValue().email().equals(email)) {
                log.warn("Remove token with email {}", email);
                iterator.remove();
            }
        }
    }

    public boolean checkByEmail(String email) {
        for (Map.Entry<String, RefreshToken> entry : repository.entrySet()) {
            if (entry.getValue().email().equals(email)) {
                log.warn("Find token with email {}", email);
                return true;
            }
        }
        return false;
    }
}

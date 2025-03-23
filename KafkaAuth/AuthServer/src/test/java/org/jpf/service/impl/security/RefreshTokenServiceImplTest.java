package org.jpf.service.impl.security;

import org.jpf.exception.security.RefreshTokenException;
import org.jpf.model.security.RefreshToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenServiceImplTest Tests")
class RefreshTokenServiceImplTest {
    private RefreshTokenServiceImpl refreshTokenService;
    private Map<String, RefreshToken> repository = new HashMap<>();
    private final Duration defaultTokenExpiry = Duration.ofMinutes(30);

    @BeforeEach
    void setUp() {
        repository = new HashMap<>();
        refreshTokenService = new RefreshTokenServiceImpl(repository);
        try {
            Field refreshTokenExpiry = RefreshTokenServiceImpl.class
                    .getDeclaredField("refreshTokenExpiry");
            refreshTokenExpiry.setAccessible(true);
            refreshTokenExpiry.set(refreshTokenService, defaultTokenExpiry);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("findByRefreshToken test: get optional Refresh Token " +
                 "from String token.")
    void givenExistingStringTokenWhenFindByRefreshTokenThenRefreshToken() {
        String token = "token";
        RefreshToken expected = new RefreshToken(
                "string@mail.com", token, Instant.now()
        );
        repository.put(token, expected);

        Optional<RefreshToken> actual =
                refreshTokenService.findByRefreshToken(token);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    @DisplayName("findByRefreshToken test: get empty optional when find " +
                 "not exists Refresh Token.")
    void givenNotExistingStringTokenWhenFindByRefreshTokenThenEmptyOptional() {
        String token = "token";

        Optional<RefreshToken> actual =
                refreshTokenService.findByRefreshToken(token);

        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("createRefreshToken test: get Refresh Token from userEmail.")
    void givenUserEmailWhenCreateRefreshTokenThenRefreshToken() {
        String userEmail = "string@mail.com";

        RefreshToken result =
                refreshTokenService.createRefreshToken(userEmail);

        assertEquals(userEmail, result.email());
        assertTrue(repository.containsKey(result.token()));
    }

    @Test
    @DisplayName("checkRefreshToken test: check RefreshToken expiry.")
    void givenCorrectRefreshTokenWhenCheckRefreshTokenThenRefreshToken() {
        String userEmail = "string@mail.com";
        RefreshToken refreshToken = new RefreshToken(
                userEmail, "token", Instant.now().plusSeconds(5)
        );

        RefreshToken actual =
                refreshTokenService.checkRefreshToken(refreshToken);

        assertDoesNotThrow(() ->
                refreshTokenService.checkRefreshToken(refreshToken)
        );

        assertEquals(refreshToken, actual);
    }

    @Test
    @DisplayName("checkRefreshToken: check incorrect RefreshToken expiry.")
    void givenIncorrectRefreshTokenWhenCheckRefreshTokenThenRefreshToken() {
        RefreshToken refreshToken = new RefreshToken(
                "string@mail.com", "token", Instant.now().minusMillis(5)
        );

        assertThrows(RefreshTokenException.class,
                () -> refreshTokenService.checkRefreshToken(refreshToken),
                " refreshToken is incorrect."
        );
    }
}


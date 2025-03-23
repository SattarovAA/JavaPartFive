package org.jpf.model.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Login request entity.")
public record LoginRequest(
        @Email(message = "Field email must email format!")
        @Schema(description = "User email", example = "string@email.com")
        String email,
        @NotNull(message = "Field confirmCode must be filled!")
        @Schema(description = "User confirm code", example = "your-code")
        String confirmCode
) {
}

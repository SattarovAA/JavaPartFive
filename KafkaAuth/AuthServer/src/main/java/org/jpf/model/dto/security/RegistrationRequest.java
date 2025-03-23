package org.jpf.model.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Registration request entity.")
public record RegistrationRequest(
        @Email(message = "Field email must email format!")
        @Schema(description = "User email", example = "string@email.com")
        String email
) {
}

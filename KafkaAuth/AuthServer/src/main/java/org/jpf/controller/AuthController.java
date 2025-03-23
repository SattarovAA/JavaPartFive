package org.jpf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jpf.model.dto.security.AuthResponse;
import org.jpf.model.dto.security.LoginRequest;
import org.jpf.model.dto.security.RefreshTokenRequest;
import org.jpf.model.dto.security.RefreshTokenResponse;
import org.jpf.model.dto.security.RegistrationRequest;
import org.jpf.model.dto.util.SimpleResponse;
import org.jpf.service.security.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

/**
 * Authentication Controller for new user registration.
 *
 * @see RegistrationRequest
 * @see LoginRequest
 * @see RefreshTokenRequest
 */
@Tag(name = "AuthenticationController",
        description = "User authentication controller.")
@RequiredArgsConstructor
@RequestMapping("api/auth")
@RestController
public class AuthController {
    private final SecurityService securityService;

    @Operation(summary = "Register new User.",
            tags = {"auth", "post", "register", "public"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = SimpleResponse.class))
    })
    @ApiResponse(responseCode = "400")
    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> registerUser(
            @RequestBody @Valid RegistrationRequest registrationRequest) {
        securityService.registerNewUser(registrationRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SimpleResponse(MessageFormat.format(
                        "Check confirm code in {0}.",
                        registrationRequest.email()
                )));
    }

    @Operation(
            summary = "Authentication user.",
            tags = {"auth", "post", "public"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = AuthResponse.class))
    })
    @ApiResponse(responseCode = "400")
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authUser(
            @RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(securityService.authenticationUser(loginRequest));
    }

    @Operation(summary = "Refresh accessToken by refreshToken.",
            tags = {"auth", "post"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = RefreshTokenResponse.class))
    })
    @ApiResponse(responseCode = "403")
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(securityService.refreshToken(request));
    }

    @Operation(summary = "Logout user.",
            tags = {"auth", "post", "logout"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = SimpleResponse.class))
    })
    @ApiResponse(responseCode = "403")
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SimpleResponse> logoutUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        securityService.logout();
        SimpleResponse simpleResponse = new SimpleResponse(
                "User logout. Username is: " + userDetails.getUsername()
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(simpleResponse);
    }
}

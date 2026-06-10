package io.github.rosstxix.flightbooking.feature.auth.controller;

import io.github.rosstxix.flightbooking.feature.auth.dto.request.LoginRequest;
import io.github.rosstxix.flightbooking.feature.auth.dto.request.RegisterRequest;
import io.github.rosstxix.flightbooking.feature.auth.dto.response.LoginResponse;
import io.github.rosstxix.flightbooking.feature.auth.service.AuthService;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ErrorResponse;
import io.github.rosstxix.flightbooking.infrastructure.openapi.ErrorApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "Authentication management")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Login",
            description = "Authenticate user and receive JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid request body",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ErrorApiResponse(status = HttpStatus.BAD_REQUEST, errorCode = ApiErrorCode.VALIDATION_ERROR, message = "email : Invalid email address")
    @ErrorApiResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ApiErrorCode.BAD_CREDENTIALS, message = "Invalid login or password")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Register", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(
                    responseCode = "400", description = "Invalid request body",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "Email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ErrorApiResponse(status = HttpStatus.BAD_REQUEST, errorCode = ApiErrorCode.VALIDATION_ERROR, message = "email : Invalid email address")
    @ErrorApiResponse(status = HttpStatus.CONFLICT, errorCode = ApiErrorCode.EMAIL_ALREADY_EXISTS, message = "User with email user@example.com already exists")
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

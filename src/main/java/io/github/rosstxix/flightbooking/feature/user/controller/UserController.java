package io.github.rosstxix.flightbooking.feature.user.controller;

import io.github.rosstxix.flightbooking.feature.user.dto.response.UserProfileResponse;
import io.github.rosstxix.flightbooking.feature.user.usecase.GetCurrentUserProfileUseCase;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ErrorResponse;
import io.github.rosstxix.flightbooking.infrastructure.openapi.ErrorApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "User management")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final GetCurrentUserProfileUseCase getCurrentUserProfileUseCase;

    public UserController(
            GetCurrentUserProfileUseCase getCurrentUserProfileUseCase
    ) {
        this.getCurrentUserProfileUseCase = getCurrentUserProfileUseCase;
    }

    @Operation(
            summary = "Get current user profile",
            description = "Returns information about the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Information about the user successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserProfileResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "Token is missing or invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "User with the specified ID was not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ErrorApiResponse(status = HttpStatus.UNAUTHORIZED, errorCode = ApiErrorCode.TOKEN_INVALID, message = "JWT token is invalid")
    @ErrorApiResponse(status = HttpStatus.NOT_FOUND, errorCode = ApiErrorCode.ENTITY_NOT_FOUND, message = "User with id 1000 was not found")
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok().body(
                getCurrentUserProfileUseCase.execute((Long) jwt.getClaims().get("userId"))
        );
    }
}

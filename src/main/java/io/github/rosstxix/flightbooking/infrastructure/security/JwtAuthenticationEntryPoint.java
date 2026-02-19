package io.github.rosstxix.flightbooking.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rosstxix.flightbooking.infrastructure.error.ErrorResponseFactory;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ErrorResponse;
import io.github.rosstxix.flightbooking.infrastructure.logging.LogMessageFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ErrorResponseFactory errorResponseFactory;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ErrorResponseFactory errorResponseFactory, ObjectMapper objectMapper) {
        this.errorResponseFactory = errorResponseFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ex
    ) throws IOException, ServletException {
        ApiErrorCode aec = resolveErrorCode(ex);

        String message = switch (aec) {
            case ApiErrorCode.TOKEN_EXPIRED -> "JWT token has expired";
            case ApiErrorCode.TOKEN_INVALID -> "JWT token is invalid";
            case ApiErrorCode.TOKEN_MISSING -> "JWT token is missing";
            default -> "Authentication failed";
        };

        ErrorResponse body = errorResponseFactory.jwtAuthError(aec, message, request.getRequestURI());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        log.warn(LogMessageFormatter.handlerError(aec, request, ex.getMessage()));

        objectMapper.writeValue(response.getOutputStream(), body);
    }


    private ApiErrorCode resolveErrorCode(AuthenticationException ex) {
        if (ex instanceof InvalidBearerTokenException) {
            Throwable cause = ex.getCause();
            if (cause instanceof JwtValidationException) {
                return ApiErrorCode.TOKEN_EXPIRED;
            }
            return ApiErrorCode.TOKEN_INVALID;
        }
        if (ex instanceof InsufficientAuthenticationException) {
            return ApiErrorCode.TOKEN_MISSING;
        }
        return ApiErrorCode.AUTHENTICATION_FAILED;
    }
}

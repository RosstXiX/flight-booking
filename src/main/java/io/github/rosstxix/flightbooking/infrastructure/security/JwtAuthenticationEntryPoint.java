package io.github.rosstxix.flightbooking.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ex
    ) throws IOException, ServletException {
        ApiErrorCode code;
        String message;

        if (ex instanceof CredentialsExpiredException) {
            code = ApiErrorCode.TOKEN_EXPIRED;
            message = "Token has expired";
        } else if (ex instanceof BadCredentialsException) {
            code = ApiErrorCode.TOKEN_INVALID;
            message = "Token is invalid";
        } else if (ex instanceof InsufficientAuthenticationException) {
            code = ApiErrorCode.TOKEN_MISSING;
            message = "Token is missing";
        } else {
            code = ApiErrorCode.AUTHENTICATION_FAILED;
            message = "Authentication failed";
        }

        ErrorResponse body = new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                code,
                message,
                request.getRequestURI()
        );
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}

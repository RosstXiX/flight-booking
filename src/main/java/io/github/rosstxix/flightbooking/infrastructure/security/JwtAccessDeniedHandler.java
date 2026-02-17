package io.github.rosstxix.flightbooking.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import io.github.rosstxix.flightbooking.infrastructure.error.model.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException ex
    ) throws IOException, ServletException {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.FORBIDDEN,
                ApiErrorCode.ACCESS_DENIED,
                "Access denied",
                request.getRequestURI()
        );

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}

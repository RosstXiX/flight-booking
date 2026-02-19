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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ErrorResponseFactory errorResponseFactory;
    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ErrorResponseFactory errorResponseFactory, ObjectMapper objectMapper) {
        this.errorResponseFactory = errorResponseFactory;
        this.objectMapper = objectMapper;
    }
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException ex
    ) throws IOException, ServletException {
        ErrorResponse body = errorResponseFactory.accessDeniedError(request.getRequestURI());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        log.warn(LogMessageFormatter.handlerError(ApiErrorCode.ACCESS_DENIED, request, ex.getMessage()));

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}

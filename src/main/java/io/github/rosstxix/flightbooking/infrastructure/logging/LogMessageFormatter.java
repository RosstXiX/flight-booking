package io.github.rosstxix.flightbooking.infrastructure.logging;

import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import jakarta.servlet.http.HttpServletRequest;

public final class LogMessageFormatter {
    private LogMessageFormatter() {}

    public static String handlerError(ApiErrorCode code, HttpServletRequest req, String message) {
        return "%s | code=%s uri=%s message=\"%s\"".formatted(code.getDescription(), code.name(), req.getRequestURI(), message);
    }

    public static String enterMethod(String className, String method, String args) {
        return "-> %s.%s() | args=\"%s\"".formatted(className, method, args);
    }

    public static String exitMethod(String className, String method, long ms) {
        return "<- %s.%s() | duration=%dms".formatted(className, method, ms);
    }

    public static String failedMethod(String className, String method, long ms, Exception ex) {
        return "X %s.%s() | duration=%dms error=%s".formatted(className, method, ms, ex.getClass().getSimpleName());
    }
}

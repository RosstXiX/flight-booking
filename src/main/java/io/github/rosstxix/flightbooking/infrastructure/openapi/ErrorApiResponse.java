package io.github.rosstxix.flightbooking.infrastructure.openapi;

import io.github.rosstxix.flightbooking.infrastructure.error.model.ApiErrorCode;
import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ErrorApiResponses.class)
public @interface ErrorApiResponse {
    HttpStatus status();
    ApiErrorCode errorCode();
    String message();
}

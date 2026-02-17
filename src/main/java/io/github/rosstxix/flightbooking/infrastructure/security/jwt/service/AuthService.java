package io.github.rosstxix.flightbooking.infrastructure.security.jwt.service;

import io.github.rosstxix.flightbooking.feature.auth.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    String authenticate(LoginRequest request);
}

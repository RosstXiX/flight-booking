package io.github.rosstxix.flightbooking.infrastructure.security.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
    String generateToken(String username);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails user);
}

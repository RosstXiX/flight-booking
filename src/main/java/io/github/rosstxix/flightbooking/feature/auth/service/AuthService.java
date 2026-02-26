package io.github.rosstxix.flightbooking.feature.auth.service;

import io.github.rosstxix.flightbooking.domain.User;
import io.github.rosstxix.flightbooking.feature.auth.dto.request.LoginRequest;
import io.github.rosstxix.flightbooking.feature.auth.dto.request.RegisterRequest;
import io.github.rosstxix.flightbooking.feature.auth.dto.response.LoginResponse;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EmailAlreadyExistsApiException;
import io.github.rosstxix.flightbooking.infrastructure.logging.aspect.Loggable;
import io.github.rosstxix.flightbooking.infrastructure.security.jwt.JwtService;
import io.github.rosstxix.flightbooking.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Loggable
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Loggable
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds()
        );
    }

    @Loggable
     public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new EmailAlreadyExistsApiException("User with email %s already exists ".formatted(registerRequest.email()));
        }

        User user = new User(
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.password()),
                registerRequest.firstName(),
                registerRequest.lastName()
        );
        userRepository.save(user);
     }
}

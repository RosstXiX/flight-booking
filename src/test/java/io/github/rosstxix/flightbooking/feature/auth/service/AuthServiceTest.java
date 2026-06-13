package io.github.rosstxix.flightbooking.feature.auth.service;

import io.github.rosstxix.flightbooking.feature.auth.dto.request.LoginRequest;
import io.github.rosstxix.flightbooking.feature.auth.dto.request.RegisterRequest;
import io.github.rosstxix.flightbooking.feature.auth.dto.response.LoginResponse;
import io.github.rosstxix.flightbooking.feature.user.domain.User;
import io.github.rosstxix.flightbooking.feature.user.repository.UserRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EmailAlreadyExistsApiException;
import io.github.rosstxix.flightbooking.infrastructure.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthService authService;

    // -------------------
    // login
    // -------------------

    @Test
    void login_shouldReturnLoginResponse_whenCredentialsValid() {
        // Arrange
        LoginRequest request = new LoginRequest("user@example.com", "password1234");
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(
                argThat(token ->
                        token instanceof UsernamePasswordAuthenticationToken t &&
                        t.getPrincipal().equals(request.email()) &&
                        t.getCredentials().equals(request.password())
                )
        )).thenReturn(authentication);

        String expectedToken = "token";
        long expectedExpiration = 3600L;

        when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);
        when(jwtService.getExpirationSeconds()).thenReturn(expectedExpiration);

        // Act
        LoginResponse response = authService.login(request);

        // Assert
        assertThat(response.accessToken()).isEqualTo(expectedToken);
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresIn()).isEqualTo(expectedExpiration);

    }

    @Test
    void login_shouldThrowBadCredentialsException_whenCredentialsInvalid() {
        // Arrange
        String expectedMessage = "Invalid credentials";
        LoginRequest request = new LoginRequest("badEmail@example.com", "password1234");

        when(authenticationManager.authenticate(
                argThat(token ->
                        token instanceof UsernamePasswordAuthenticationToken t &&
                        t.getPrincipal().equals(request.email()) &&
                        t.getCredentials().equals(request.password())
                )
        )).thenThrow(new BadCredentialsException(expectedMessage));

        // Act + Assert
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage(expectedMessage);

        verifyNoInteractions(jwtService);
    }

    // -------------------
    // register
    // -------------------

    @Test
    void register_shouldSaveUserWithEncodedPassword_whenEmailNotExists() {
        // Arrange
        RegisterRequest request = buildRegisterRequest();
        String encodedPassword = "encodedPassword";

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);

        // Act
        authService.register(request);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveAndFlush(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo(request.email());
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(savedUser.getFirstName()).isEqualTo(request.firstName());
        assertThat(savedUser.getLastName()).isEqualTo(request.lastName());
    }

    @Test
    void register_shouldThrowEmailAlreadyExistsApiException_whenEmailExists() {
        // Arrange
        RegisterRequest request = buildRegisterRequest();

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(EmailAlreadyExistsApiException.class)
                .hasMessageContaining(request.email());

        verify(userRepository, never()).saveAndFlush(any());
    }

    private static RegisterRequest buildRegisterRequest() {
        return new RegisterRequest(
                "user@example.com", "password1234", "John", "Doe"
        );
    }


}

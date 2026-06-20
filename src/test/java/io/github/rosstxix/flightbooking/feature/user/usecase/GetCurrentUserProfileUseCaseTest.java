package io.github.rosstxix.flightbooking.feature.user.usecase;

import io.github.rosstxix.flightbooking.feature.user.dto.projection.UserProfileProjection;
import io.github.rosstxix.flightbooking.feature.user.dto.response.UserProfileResponse;
import io.github.rosstxix.flightbooking.feature.user.repository.UserRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCurrentUserProfileUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private GetCurrentUserProfileUseCase getCurrentUserProfileUseCase;

    @Test
    void execute_shouldReturnUserProfile_whenHappyPath() {
        // Arrange
        Long userId = 1L;
        UserProfileProjection projection = mock(UserProfileProjection.class);

        when(projection.getEmail()).thenReturn("john@example.com");
        when(projection.getFirstName()).thenReturn("John");
        when(projection.getLastName()).thenReturn("Doe");
        when(userRepository.findProfileProjectionByUserId(userId))
                .thenReturn(Optional.of(projection));

        // Act
        UserProfileResponse result = getCurrentUserProfileUseCase.execute(userId);

        // Assert
        assertThat(result.email()).isEqualTo("john@example.com");
        assertThat(result.firstName()).isEqualTo("John");
        assertThat(result.lastName()).isEqualTo("Doe");
    }

    @Test
    void execute_shouldThrowEntityNotFoundApiException_whenUserNotFound() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findProfileProjectionByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> getCurrentUserProfileUseCase.execute(userId))
                .isInstanceOf(EntityNotFoundApiException.class)
                .hasMessageContaining(String.valueOf(userId));
    }
}

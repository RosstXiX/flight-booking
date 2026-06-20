package io.github.rosstxix.flightbooking.feature.user.usecase;

import io.github.rosstxix.flightbooking.feature.user.dto.projection.UserProfileProjection;
import io.github.rosstxix.flightbooking.feature.user.dto.response.UserProfileResponse;
import io.github.rosstxix.flightbooking.feature.user.repository.UserRepository;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.EntityNotFoundApiException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetCurrentUserProfileUseCase {

    private final UserRepository userRepository;

    public GetCurrentUserProfileUseCase(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse execute(
            Long userId
    ) {
        UserProfileProjection projection = userRepository.findProfileProjectionByUserId(userId).orElseThrow(
                () -> new EntityNotFoundApiException("User with id %d not found".formatted(userId))
        );

        return new UserProfileResponse(projection.getEmail(), projection.getFirstName(), projection.getLastName());
    }
}

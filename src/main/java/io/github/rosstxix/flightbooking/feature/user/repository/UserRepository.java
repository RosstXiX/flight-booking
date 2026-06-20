package io.github.rosstxix.flightbooking.feature.user.repository;

import io.github.rosstxix.flightbooking.feature.user.domain.User;
import io.github.rosstxix.flightbooking.feature.user.dto.projection.UserProfileProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("""
            SELECT
                  u.email AS email,
                  u.firstName AS firstName,
                  u.lastName AS lastName
            FROM User u
            WHERE u.id = :id
            """)
    Optional<UserProfileProjection> findProfileProjectionByUserId(
            @Param("id") Long id
    );
}

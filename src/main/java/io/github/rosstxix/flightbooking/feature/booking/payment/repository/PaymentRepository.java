package io.github.rosstxix.flightbooking.feature.booking.payment.repository;

import io.github.rosstxix.flightbooking.feature.booking.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}

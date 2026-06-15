package io.github.rosstxix.flightbooking.feature.booking.payment.domain;

import io.github.rosstxix.flightbooking.common.domain.Auditable;
import io.github.rosstxix.flightbooking.feature.booking.domain.Booking;
import io.github.rosstxix.flightbooking.infrastructure.error.exception.InvalidPaymentStateApiException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
public class Payment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    public Payment (
            BigDecimal amount,
            String currency,
            Booking booking
    ) {
        this.amount = amount;
        this.currency = currency;
        this.booking = booking;
        this.status = PaymentStatus.SUCCESS;
    }

    public void refund() {
        if (this.status != PaymentStatus.SUCCESS) {
            throw new InvalidPaymentStateApiException("Payment with id %d is not in success state".formatted(this.id));
        }
        this.status = PaymentStatus.REFUNDED;
    }
}

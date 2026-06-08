CREATE TABLE payments
(
    id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    amount     NUMERIC(10, 2)           NOT NULL CHECK (amount >= 0),
    currency   VARCHAR(3)               NOT NULL,
    booking_id BIGINT                   NOT NULL UNIQUE REFERENCES bookings (id),
    status     VARCHAR(20)              NOT NULL CHECK (status IN ('SUCCESS', 'REFUNDED')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
)
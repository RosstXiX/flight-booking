ALTER TABLE flights
    ADD COLUMN currency        VARCHAR(3) NOT NULL DEFAULT 'USD',
    ADD COLUMN available_seats INTEGER    NOT NULL DEFAULT 0,
    ADD COLUMN version         BIGINT     NOT NULL DEFAULT 0;

ALTER TABLE flights
    ADD CONSTRAINT chk_flights_available_seats_non_negative
        CHECK (available_seats >= 0);
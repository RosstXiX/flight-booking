CREATE TABLE airports
(
    id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    code       VARCHAR(3)               NOT NULL UNIQUE,
    name       VARCHAR(255)             NOT NULL,
    city       VARCHAR(255)             NOT NULL,
    country    VARCHAR(255)             NOT NULL,
    timezone   VARCHAR(50)              NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE aircrafts
(
    id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    model       VARCHAR(255)             NOT NULL,
    total_seats INTEGER                  NOT NULL CHECK (total_seats > 0),
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE users
(
    id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    email      VARCHAR(255)             NOT NULL UNIQUE,
    password   VARCHAR(255)             NOT NULL,
    first_name VARCHAR(255)             NOT NULL,
    last_name  VARCHAR(255)             NOT NULL,
    role       VARCHAR(50)              NOT NULL DEFAULT 'USER'
        CHECK (role IN ('USER', 'ADMIN')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE flights
(
    id                   BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    flight_number        VARCHAR(20)              NOT NULL UNIQUE,
    departure_airport_id BIGINT                   NOT NULL REFERENCES airports (id),
    arrival_airport_id   BIGINT                   NOT NULL REFERENCES airports (id),
    aircraft_id          BIGINT                   NOT NULL REFERENCES aircrafts (id),
    departure_utc        TIMESTAMP WITH TIME ZONE NOT NULL,
    arrival_utc          TIMESTAMP WITH TIME ZONE NOT NULL,
    price                NUMERIC(10, 2)           NOT NULL CHECK (price > 0),
    status               VARCHAR(20)              NOT NULL DEFAULT 'SCHEDULED'
        CHECK (status IN ('SCHEDULED', 'DEPARTED', 'ARRIVED', 'CANCELLED', 'DELAYED')),
    created_at           TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at           TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE bookings
(
    id           BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id      BIGINT                   NOT NULL REFERENCES users (id),
    flight_id    BIGINT                   NOT NULL REFERENCES flights (id),
    seat_number  VARCHAR(5)               NOT NULL,
    booking_date TIMESTAMP WITH TIME ZONE NOT NULL,
    status       VARCHAR(20)              NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED')),
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL
);

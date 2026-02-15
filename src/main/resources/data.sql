-- Airports
INSERT INTO airports (code, name, city, country, timezone)
VALUES ('KBP', 'Boryspil International Airport', 'Kyiv', 'Ukraine', 'Europe/Kiev'),
       ('LWO', 'Lviv Danylo Halytskyi International Airport', 'Lviv', 'Ukraine', 'Europe/Kiev'),
       ('ODS', 'Odesa International Airport', 'Odesa', 'Ukraine', 'Europe/Kiev'),
       ('LHR', 'London Heathrow', 'London', 'United Kingdom', 'Europe/London'),
       ('JFK', 'John F. Kennedy International Airport', 'New York', 'USA', 'America/New_York');

-- Aircraft types
INSERT INTO aircrafts (model, total_seats)
VALUES ('Boeing 737-800', 189),
       ('Airbus A320', 180),
       ('Embraer E190', 114);

-- Flights (UTC timestamps)
-- KBP → LWO: depart 08:00 Kiev time = 06:00 UTC (winter) / 05:00 UTC (summer)
-- For simplicity, using winter time (UTC+2)
INSERT INTO flights (flight_number, departure_airport_id, arrival_airport_id, aircraft_id, departure_utc, arrival_utc,
                     price, status)
VALUES
-- Kyiv → Lviv
('PS101', 1, 2, 1, '2026-02-20 06:00:00+00', '2026-02-20 07:15:00+00', 1500.00, 'SCHEDULED'),
('PS102', 1, 2, 1, '2026-02-20 12:00:00+00', '2026-02-20 13:15:00+00', 1500.00, 'SCHEDULED'),
-- Lviv → Kyiv
('PS201', 2, 1, 1, '2026-02-20 08:00:00+00', '2026-02-20 09:15:00+00', 1500.00, 'SCHEDULED'),
('PS202', 2, 1, 1, '2026-02-20 14:00:00+00', '2026-02-20 15:15:00+00', 1500.00, 'SCHEDULED'),
-- Kyiv → Odesa
('PS301', 1, 3, 2, '2026-02-20 09:00:00+00', '2026-02-20 10:30:00+00', 1800.00, 'SCHEDULED'),
-- Kyiv → London
('PS401', 1, 4, 1, '2026-02-21 05:00:00+00', '2026-02-21 07:30:00+00', 5000.00, 'SCHEDULED'),
-- Kyiv → New York
('PS501', 1, 5, 1, '2026-02-22 08:00:00+00', '2026-02-22 19:00:00+00', 12000.00, 'SCHEDULED');

INSERT INTO users (email, password, first_name, last_name, role)
VALUES ('admin@example.com', '$2a$10$wr7TMVbDJxUbjoj7m/Z7fODcSs3eFH5uC2AVPEhp6Ekq9rmn0/jIy', 'Admin', 'User', 'ADMIN'),
       ('user@example.com', '$2a$10$wr7TMVbDJxUbjoj7m/Z7fODcSs3eFH5uC2AVPEhp6Ekq9rmn0/jIy', 'Test', 'User', 'USER');
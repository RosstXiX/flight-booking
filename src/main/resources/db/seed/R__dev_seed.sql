-- Airports
INSERT INTO airports (code, name, city, country, timezone, created_at, updated_at)
VALUES ('KBP', 'Boryspil International Airport', 'Kyiv', 'Ukraine', 'Europe/Kyiv', '2026-02-26 21:00:00+00',
        '2026-02-26 21:00:00+00'),
       ('LWO', 'Lviv Danylo Halytskyi International Airport', 'Lviv', 'Ukraine', 'Europe/Kyiv',
        '2026-02-26 21:00:00+00', '2026-02-26 21:00:00+00'),
       ('ODS', 'Odesa International Airport', 'Odesa', 'Ukraine', 'Europe/Kyiv', '2026-02-26 21:00:00+00',
        '2026-02-26 21:00:00+00'),
       ('LHR', 'London Heathrow', 'London', 'United Kingdom', 'Europe/London', '2026-02-26 21:00:00+00',
        '2026-02-26 21:00:00+00'),
       ('JFK', 'John F. Kennedy International Airport', 'New York', 'USA', 'America/New_York', '2026-02-26 21:00:00+00',
        '2026-02-26 21:00:00+00');

-- Aircraft types
INSERT INTO aircrafts (model, total_seats, created_at, updated_at)
VALUES ('Boeing 737-800', 189, '2026-02-26 21:00:00+00', '2026-02-26 21:00:00+00'),
       ('Airbus A320', 180, '2026-02-26 21:00:00+00', '2026-02-26 21:00:00+00'),
       ('Embraer E190', 114, '2026-02-26 21:00:00+00', '2026-02-26 21:00:00+00');

-- Flights (UTC timestamps)
-- For simplicity, assuming UTC+2 (no DST handling)
INSERT INTO flights (flight_number, departure_airport_id, arrival_airport_id, aircraft_id, departure_utc, arrival_utc,
                     price, status, created_at, updated_at)
VALUES
-- Kyiv -> Lviv
('PS101', 1, 2, 1, '2026-10-20 06:00:00+00', '2026-10-20 07:15:00+00', 1500.00, 'SCHEDULED', '2026-02-26 21:00:00+00',
 '2026-02-26 21:00:00+00'),
('PS102', 1, 2, 1, '2026-10-20 12:00:00+00', '2026-10-20 13:15:00+00', 1700.00, 'SCHEDULED', '2026-02-26 21:00:00+00',
 '2026-02-26 21:00:00+00'),
-- Lviv -> Kyiv
('PS201', 2, 1, 1, '2026-10-20 08:00:00+00', '2026-10-20 09:15:00+00', 1500.00, 'SCHEDULED', '2026-02-26 21:00:00+00',
 '2026-02-26 21:00:00+00'),
('PS202', 2, 1, 1, '2026-10-20 14:00:00+00', '2026-10-20 15:15:00+00', 1700.00, 'SCHEDULED', '2026-02-26 21:00:00+00',
 '2026-02-26 21:00:00+00'),

-- Kyiv -> Odesa
('PS301', 1, 3, 2, '2026-10-20 07:00:00+00', '2026-10-20 08:30:00+00', 1800.00, 'SCHEDULED', '2026-02-26 21:00:00+00',
 '2026-02-26 21:00:00+00'),
-- Odesa -> Kyiv
('PS302', 3, 1, 2, '2026-10-20 09:15:00+00', '2026-10-20 10:45:00+00', 1800.00, 'SCHEDULED', '2026-02-26 21:00:00+00',
 '2026-02-26 21:00:00+00'),

-- Kyiv -> London
('PS401', 1, 4, 3, '2026-10-21 05:00:00+00', '2026-10-21 07:30:00+00', 5000.00, 'SCHEDULED', '2026-02-26 21:00:00+00',
 '2026-02-26 21:00:00+00'),
-- London -> Kyiv
('PS402', 4, 1, 3, '2026-10-21 09:00:00+00', '2026-10-21 13:30:00+00', 5200.00, 'SCHEDULED', '2026-02-26 21:00:00+00',
 '2026-02-26 21:00:00+00'),

-- Kyiv -> New York
('PS501', 1, 5, 3, '2026-10-22 08:00:00+00', '2026-10-22 19:00:00+00', 12000.00, 'SCHEDULED', '2026-02-26 21:00:00+00',
 '2026-02-26 21:00:00+00'),
-- New York -> Kyiv
('PS502', 5, 1, 3, '2026-10-23 01:00:00+00', '2026-10-23 11:00:00+00', 12500.00, 'SCHEDULED', '2026-02-26 21:00:00+00',
 '2026-02-26 21:00:00+00');

INSERT INTO users (email, password, first_name, last_name, role, created_at, updated_at)
VALUES ('admin@example.com', '$2a$10$WY/SN8/qCMcMYc.Lr8A0ceYAL9i2I5JdcyC7IYIIoQOeySs2ZRwTS', 'Admin', 'User', 'ADMIN',
        '2026-02-26 21:00:00+00', '2026-02-26 21:00:00+00'),
       ('user@example.com', '$2a$10$WY/SN8/qCMcMYc.Lr8A0ceYAL9i2I5JdcyC7IYIIoQOeySs2ZRwTS', 'Test', 'User', 'USER',
        '2026-02-26 21:00:00+00', '2026-02-26 21:00:00+00');
ALTER TABLE bookings DROP COLUMN booking_date;

CREATE UNIQUE INDEX uq_bookings_flight_seat_active
    ON bookings (flight_id, seat_number)
    WHERE status <> 'CANCELLED';
ALTER TABLE aircrafts
    ADD COLUMN seat_layout          VARCHAR(9) NOT NULL DEFAULT '',
    ADD COLUMN rows                 INT        NOT NULL DEFAULT 0,
    ADD COLUMN seat_per_row         INT        NOT NULL DEFAULT 0,
    ADD COLUMN premium_seat_layout  VARCHAR(9) NOT NULL DEFAULT '',
    ADD COLUMN premium_rows         INT        NOT NULL DEFAULT 0,
    ADD COLUMN seat_per_premium_row INT        NOT NULL DEFAULT 0;
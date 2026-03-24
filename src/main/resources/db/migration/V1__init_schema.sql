CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(50)
);


------------------------------------------------------------



CREATE TABLE hall (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(255),
                      rows INTEGER NOT NULL,
                      columns INTEGER NOT NULL
);



------------------------------------------------------------




CREATE TABLE movies (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        poster_url VARCHAR(255) NOT NULL,
                        slug VARCHAR(255) NOT NULL UNIQUE
);

------------------------------------------------------------




CREATE TABLE screening (
                           id BIGSERIAL PRIMARY KEY,
                           movie_id BIGINT,
                           hall_id BIGINT,
                           start_time TIMESTAMP,

                           CONSTRAINT fk_screening_movie
                               FOREIGN KEY (movie_id)
                                   REFERENCES movies(id)
                                   ON DELETE SET NULL,

                           CONSTRAINT fk_screening_hall
                               FOREIGN KEY (hall_id)
                                   REFERENCES hall(id)
                                   ON DELETE SET NULL
);

------------------------------------------------------------




CREATE TABLE seat_reservation (
                                  id BIGSERIAL PRIMARY KEY,
                                  row_number INTEGER NOT NULL,
                                  column_number INTEGER NOT NULL,
                                  screening_id BIGINT,
                                  owner_id BIGINT,

                                  CONSTRAINT fk_reservation_screening
                                      FOREIGN KEY (screening_id)
                                          REFERENCES screening(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_reservation_user
                                      FOREIGN KEY (owner_id)
                                          REFERENCES users(id)
                                          ON DELETE SET NULL,

                                  CONSTRAINT unique_seat_per_screening
                                      UNIQUE (screening_id, row_number, column_number)
);

------------------------------------------------------------




CREATE INDEX idx_screening_movie ON screening(movie_id);
CREATE INDEX idx_screening_hall ON screening(hall_id);
CREATE INDEX idx_reservation_screening ON seat_reservation(screening_id);
CREATE INDEX idx_reservation_owner ON seat_reservation(owner_id);
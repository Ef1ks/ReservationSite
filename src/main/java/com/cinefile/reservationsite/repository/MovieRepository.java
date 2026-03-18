package com.cinefile.reservationsite.repository;

import com.cinefile.reservationsite.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}

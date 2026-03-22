package com.cinefile.reservationsite.repository;

import com.cinefile.reservationsite.dto.MovieLightDto;
import com.cinefile.reservationsite.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT new com.cinefile.reservationsite.dto.MovieLightDto(m.title, m.slug, m.posterUrl) FROM Movie m")
    List<MovieLightDto> findAllLightMovies();

    boolean existsBySearchKey(String searchKey);
}

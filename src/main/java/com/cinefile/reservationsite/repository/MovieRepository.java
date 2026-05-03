package com.cinefile.reservationsite.repository;

import com.cinefile.reservationsite.dto.movie.MovieLightDto;
import com.cinefile.reservationsite.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
    @Query("SELECT new com.cinefile.reservationsite.dto.movie.MovieLightDto(m.id ,m.title, m.slug, m.posterUrl) FROM Movie m")
    List<MovieLightDto> findAllLightMovies();

    Optional<Movie> findBySlug(String slug);


    boolean existsBySearchKey(String searchKey);
}

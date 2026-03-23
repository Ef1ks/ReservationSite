package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.CreateMovieRequest;
import com.cinefile.reservationsite.dto.MovieLightDto;
import com.cinefile.reservationsite.model.Movie;
import com.cinefile.reservationsite.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    @Transactional
    public void createMovie(CreateMovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .posterUrl(request.getPosterUrl())
                .build();

        movieRepository.save(movie);
        log.info("Dodano nowy film '{}' do bazy danych z plakatem: {}", movie.getTitle(), movie.getPosterUrl());
    }

    public List<MovieLightDto> getMoviesForHomePage() {
        return movieRepository.findAllLightMovies();
    }
}
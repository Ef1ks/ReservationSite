package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.movie.CreateMovieRequest;
import com.cinefile.reservationsite.dto.MovieLightDto;
import com.cinefile.reservationsite.model.Movie;
import com.cinefile.reservationsite.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.text.Normalizer;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    @Transactional
    public void createMovie(CreateMovieRequest request) {
        String searchKey = normalizeTitle(request.getTitle());
        if (movieRepository.existsBySearchKey(searchKey)) {
            log.error("Film już istnieje");
            return;
        }

        Movie movie = Movie.builder()
                .title(request.getTitle())
                .posterUrl(request.getPosterUrl())
                .searchKey(searchKey)
                .length(request.getLenght())
                .build();

        movieRepository.save(movie);
        log.info("Dodano nowy film '{}' do bazy danych z plakatem: {}", movie.getTitle(), movie.getPosterUrl());
    }

    public List<MovieLightDto> getMoviesForHomePage() {
        return movieRepository.findAllLightMovies();
    }

    private String normalizeTitle(String title) {
        if (title == null || title.isBlank()) {
            return "";
        }
        String preProcessed = title.replace("ł", "l").replace("Ł", "L");
        String normalized = Normalizer.normalize(preProcessed, Normalizer.Form.NFD);
        return normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "");
    }
}
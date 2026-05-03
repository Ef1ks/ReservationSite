package com.cinefile.reservationsite.controller;

import com.cinefile.reservationsite.dto.MovieUploadResponse;
import com.cinefile.reservationsite.dto.movie.*;
import com.cinefile.reservationsite.service.MovieService;
import com.cinefile.reservationsite.service.OmdbService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final OmdbService omdbService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieUploadResponse createMovie(@Valid @RequestBody CreateMovieRequest request) {
        movieService.createMovie(request);
        return new MovieUploadResponse("Film został dodany!");
    }

    @GetMapping("/getMovies")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieLightDto> getMovies() {
        return movieService.getMoviesForHomePage();
    }

    @GetMapping("/getSingleMovie/{slug}")
    @ResponseStatus(HttpStatus.OK)
    public FullMovieInfoDto getSingleMovie(@PathVariable String slug) {
        return movieService.getMovie(slug);
    }


    @GetMapping("/searchOmdb")
    public List<MovieSearchDto> searchOmdb(@RequestParam String title, @RequestParam(required = false) String year) {
        if (title == null || title.trim().length() < 3) {
            return Collections.emptyList();
        }
        return omdbService.searchMovie(title, year);
    }

    @GetMapping("/getOmdbMovieDetails")
    public MovieDetailsDto getOmdbMovieDetails(@RequestParam String imdbId) {
        return omdbService.getMovieDetails(imdbId);
    }


}



package com.cinefile.reservationsite.controller;

import com.cinefile.reservationsite.dto.CreateMovieRequest;
import com.cinefile.reservationsite.dto.MovieLightDto;
import com.cinefile.reservationsite.model.Movie;
import com.cinefile.reservationsite.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMovie(@Valid @RequestBody CreateMovieRequest request) {
        movieService.createMovie(request);
    }

    @GetMapping("/getMovies")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieLightDto> getMovies() {
        return movieService.getMoviesForHomePage();
    }
}



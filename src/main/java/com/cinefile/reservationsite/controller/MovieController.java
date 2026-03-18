package com.cinefile.reservationsite.controller;

import com.cinefile.reservationsite.dto.CreateMovieRequest;
import com.cinefile.reservationsite.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}



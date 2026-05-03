package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.movie.MovieDetailsDto;
import com.cinefile.reservationsite.dto.movie.MovieSearchDto;
import com.cinefile.reservationsite.dto.movie.OmdbSearchResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Service
public class OmdbService {
    private final RestClient restClient;
    private final String apiKey;

    public OmdbService(@Value("${omdb.api.key}") String apiKey, @Value("${omdb.api.url}") String apiUrl) {
        this.restClient = RestClient.builder().baseUrl(apiUrl).build();
        this.apiKey = apiKey;
    }

    public List<MovieSearchDto> searchMovie(String title, String year) {
        OmdbSearchResponseDto response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.queryParam("apikey", apiKey)
                            .queryParam("s", title)
                            .queryParam("type", "movie");
                    if (year != null && year.trim().length() == 4) {
                        uriBuilder.queryParam("y", year.trim());
                    }

                    return uriBuilder.build();
                })
                .retrieve()
                .body(OmdbSearchResponseDto.class);

        System.out.println(response);

        if (response != null && response.search() != null) {
            return response.search();
        }
        return Collections.emptyList();
    }

    public MovieDetailsDto getMovieDetails(String imdbId) {
        MovieDetailsDto response = restClient.get()
                .uri(uriBuilder -> {
                    return uriBuilder
                            .queryParam("apikey", apiKey)
                            .queryParam("i", imdbId)
                            .queryParam("plot", "full")
                            .build();
                })
                .retrieve()
                .body(MovieDetailsDto.class);
        System.out.println(response);
    return response;}
}

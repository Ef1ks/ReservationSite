package com.cinefile.reservationsite.dto.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;

public record MovieDetailsDto(
        // 1. Identyfikacja
        @JsonProperty("imdbID")
        String imdbId,

        @JsonProperty("Title")
        String title,

        @JsonProperty("Year")
        String year,

        // 2. Multimedia
        @JsonProperty("Poster")
        String poster,

        // 3. Opis, klimat i pochodzenie
        @JsonProperty("Plot")
        String plot,

        @JsonProperty("Genre")
        String genre,

        @JsonProperty("Language")
        String language,

        @JsonProperty("Country")
        String country,

        // 4. Ekipa
        @JsonProperty("Director")
        String director,

        @JsonProperty("Actors")
        String actors,

        // 5. Dodatkowe informacje
        @JsonProperty("Runtime")
        String runtime,

        @JsonProperty("imdbRating")
        String imdbRating,

        @JsonProperty("Released")
        String releaseDate
) {}
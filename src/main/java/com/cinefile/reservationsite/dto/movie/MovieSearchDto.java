package com.cinefile.reservationsite.dto.movie;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieSearchDto(
        @JsonProperty("Title")
        String title,

        @JsonProperty("Year")
        String year,

        @JsonProperty("imdbID")
        String imdbId,

        @JsonProperty("Poster")
        String poster
) {
}
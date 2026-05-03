package com.cinefile.reservationsite.dto.movie;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OmdbSearchResponseDto(
        @JsonProperty("Search")
        List<MovieSearchDto> search,

        @JsonProperty("totalResults")
        String totalResults,

        @JsonProperty("Response")
        String response
) {
}
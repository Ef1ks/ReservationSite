package com.cinefile.reservationsite.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMovieRequest(
        @NotBlank(message = "Tytuł nie może być pusty")
        String title,

        @NotBlank(message = "Link do plakatu jest wymagany")
        String posterUrl,

        @NotNull(message = "Długość filmu jest wymagana")
        int duration
) {
}
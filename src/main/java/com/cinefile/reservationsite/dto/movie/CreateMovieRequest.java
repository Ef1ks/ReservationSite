package com.cinefile.reservationsite.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMovieRequest {

    @NotBlank(message = "Tytuł nie może być pusty")
    private String title;
    @NotBlank(message = "Link do plakatu jest wymagany")
    private String posterUrl;
    @NotNull(message = "Długość filmu jest wymagana")
    private int duration;
}
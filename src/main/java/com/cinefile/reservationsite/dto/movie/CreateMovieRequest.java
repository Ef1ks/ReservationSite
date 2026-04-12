package com.cinefile.reservationsite.dto.movie;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMovieRequest {

    @NotBlank(message = "Tytuł nie może być pusty")
    private String title;
    @NotBlank(message = "Link do plakatu jest wymagany")
    private String posterUrl;
    @NotBlank(message = "Długość filmu jest wymagana")
    private int lenght;
}
package com.cinefile.reservationsite.dto.login;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequest(
        @NotBlank(message = "Token nie może być pusty")
        String token) {
}

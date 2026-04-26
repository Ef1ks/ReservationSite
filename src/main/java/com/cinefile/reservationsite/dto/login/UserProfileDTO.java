package com.cinefile.reservationsite.dto.login;


public record UserProfileDTO(
        Long id,
        String email,
        String role
) {
}
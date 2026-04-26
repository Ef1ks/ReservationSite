package com.cinefile.reservationsite.dto.login;

public record AuthResult(
        String jwtToken,
        UserProfileDTO userProfile
) {
}

package com.cinefile.reservationsite.dto.movie;

import java.util.UUID;

public record MovieLightDto(
        UUID id,
        String title,
        String slug,
        String posterUrl
) {
}

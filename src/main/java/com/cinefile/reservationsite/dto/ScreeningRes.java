package com.cinefile.reservationsite.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScreeningRes(
        UUID movieId,
        Long hallId,
        LocalDateTime startTime
) {
}

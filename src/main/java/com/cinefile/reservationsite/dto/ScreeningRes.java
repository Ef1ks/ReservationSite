package com.cinefile.reservationsite.dto;

import java.time.LocalDateTime;

public record ScreeningRes(
        Long movieId,
        Long hallId,
        LocalDateTime startTime
) {}

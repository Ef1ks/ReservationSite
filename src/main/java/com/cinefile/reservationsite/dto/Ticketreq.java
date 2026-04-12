package com.cinefile.reservationsite.dto;

public record Ticketreq(
        int row,
        int col,
        Long screeningId,
        String ownerEmail,
        String ownerName,
        String ownerSurname
) {
}

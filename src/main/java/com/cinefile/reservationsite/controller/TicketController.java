package com.cinefile.reservationsite.controller;

import com.cinefile.reservationsite.dto.ScreeningRes;
import com.cinefile.reservationsite.dto.Ticketreq;
import com.cinefile.reservationsite.security.UserPrincipal;
import com.cinefile.reservationsite.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTicket(@Valid @RequestBody List<Ticketreq> req, @AuthenticationPrincipal UserPrincipal principal) {
        ticketService.createTickets(req,principal.getId());
    }


}

package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.ScreeningRes;
import com.cinefile.reservationsite.dto.Ticketreq;
import com.cinefile.reservationsite.model.Login.User;
import com.cinefile.reservationsite.model.Screening;
import com.cinefile.reservationsite.model.Ticket;
import com.cinefile.reservationsite.repository.ScreeningRepository;
import com.cinefile.reservationsite.repository.TicketRepository;
import com.cinefile.reservationsite.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ScreeningRepository screeningRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    @Transactional
    public void createTickets(List<Ticketreq> ticketRequests,Long userId) {
        User user = userRepository.getReferenceById(userId);
        List<Ticket> tickets=new ArrayList<>();
        for (Ticketreq ticketreq : ticketRequests) {

            Ticket ticket = new Ticket();

            Screening screening = screeningRepository
                    .getReferenceById(ticketreq.screeningId());

            boolean exists = ticketRepository
                    .existsByRowAndColAndScreening(
                            ticketreq.row(),
                            ticketreq.col(),
                            screening
                    );

            if (exists) {
                log.error("Miejsce jest zarezerwowane BE");
                throw new EntityExistsException(
                        "Miejsce na ten seans jest już zarezerwowane  BE"
                );
            }

            ticket.setRow(ticketreq.row());
            ticket.setCol(ticketreq.col());

            ticket.setPaid(false);
            ticket.setScreening(screening);
            ticket.setOwner(user);
            ticket.setOwnerEmail(user.getEmail());
            tickets.add(ticketRepository.save(ticket));

            //TODO RATELIMITY i Mailsender
        }
        emailService.sendTicketConfirmation(user.getEmail(),tickets);
    }
}

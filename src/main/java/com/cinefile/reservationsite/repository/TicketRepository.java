package com.cinefile.reservationsite.repository;

import com.cinefile.reservationsite.model.Screening;
import com.cinefile.reservationsite.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    boolean existsByRowAndColAndScreening(int row, int col, Screening screening);

    List<Ticket> getAllByScreeningAndPaid(Screening screening,boolean paid);
}

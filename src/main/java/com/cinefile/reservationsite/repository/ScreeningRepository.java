package com.cinefile.reservationsite.repository;

import com.cinefile.reservationsite.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

//TODO add methods to find beetween 2 dates
public interface ScreeningRepository extends JpaRepository<Screening,Long> {

    List<Screening> findBystartTimeBetween(LocalDateTime sD, LocalDateTime eD);
}

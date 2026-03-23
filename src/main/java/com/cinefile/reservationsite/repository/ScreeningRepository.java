package com.cinefile.reservationsite.repository;

import com.cinefile.reservationsite.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
//TODO add methods to find beetween 2 dates
public interface ScreeningRepository extends JpaRepository<Screening,Long> {
}

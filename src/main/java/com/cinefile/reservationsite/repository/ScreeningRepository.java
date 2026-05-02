package com.cinefile.reservationsite.repository;

import com.cinefile.reservationsite.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findBystartTimeBetween(LocalDateTime sD, LocalDateTime eD);

    boolean existsByStartTimeAndHall_IdAndMovie_Id(LocalDateTime startTime, Long hallId, UUID movieId);

    boolean existsByHall_IdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long hallId,
            LocalDateTime endTime,
            LocalDateTime startTime
    );
}

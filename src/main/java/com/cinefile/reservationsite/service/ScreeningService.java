package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.ScreeningRes;
import com.cinefile.reservationsite.model.Screening;
import com.cinefile.reservationsite.repository.HallRepository;
import com.cinefile.reservationsite.repository.MovieRepository;
import com.cinefile.reservationsite.repository.ScreeningRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScreeningService {
    final private ScreeningRepository screeningRepository;
    final private HallRepository hallRepository;
    final private MovieRepository movieRepository;

    private static ScreeningRes toResponse(Screening b) {
        return new ScreeningRes(
                b.getMovie().getId(),
                b.getHall().getId(),
                b.getStartTime()
        );
    }

    @Transactional
    public void addScreening(ScreeningRes screeningRes) {

        Screening screening = new Screening();

        LocalDateTime start = screeningRes.startTime();
        int duration = movieRepository.getReferenceById(screeningRes.movieId()).getDuration();
        LocalDateTime end = start.plusMinutes(duration);

        screening.setHall(hallRepository.getReferenceById(screeningRes.hallId()));
        screening.setMovie(movieRepository.getReferenceById(screeningRes.movieId()));
        screening.setStartTime(screeningRes.startTime());
        screening.setEndTime(end);

        boolean exists = screeningRepository.existsByStartTimeAndHall_IdAndMovie_Id(screeningRes.startTime(), screeningRes.hallId(), screeningRes.movieId());


        boolean hallIsOccupied = screeningRepository.existsByHall_IdAndStartTimeLessThanAndEndTimeGreaterThan(
                screeningRes.hallId(),
                end,
                start
        );

        if (exists || hallIsOccupied) {
            log.error("Film już istnieje lub hala jest zajęta");
            throw new EntityExistsException(" już zajęty.");
        }
        screeningRepository.save(screening);
    }

    @Transactional
    public List<ScreeningRes> getScreeningsBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return screeningRepository.findBystartTimeBetween(startDate, endDate)
                .stream()
                .map(ScreeningService::toResponse)
                .toList();
    }

    @Transactional
    public List<ScreeningRes> getScreenings() {

        return screeningRepository.findAll()
                .stream()
                .map(ScreeningService::toResponse)
                .toList();
    }
}

package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.ScreeningRes;
import com.cinefile.reservationsite.model.Screening;
import com.cinefile.reservationsite.repository.HallRepository;
import com.cinefile.reservationsite.repository.MovieRepository;
import com.cinefile.reservationsite.repository.ScreeningRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {
    final private ScreeningRepository screeningRepository;
    final private HallRepository hallRepository;
    final private MovieRepository movieRepository;

    @Transactional
    public void addScreening(ScreeningRes screeningRes) {
        Screening screening = new Screening();
        screening.setHall(hallRepository.getReferenceById(screeningRes.hallId()));
        screening.setMovie(movieRepository.getReferenceById(screeningRes.movieId()));
        screening.setStartTime(screeningRes.startTime());
        screeningRepository.save(screening);
    }

    @Transactional
    public List<ScreeningRes> getScreenings() {

        return screeningRepository.findAll()
                .stream()
                .map(ScreeningService::toResponse)
                .toList();
    }

    private static ScreeningRes toResponse(Screening b) {
        return new ScreeningRes(
                b.getMovie().getId(),
                b.getHall().getId(),
                b.getStartTime()
        );
    }
}

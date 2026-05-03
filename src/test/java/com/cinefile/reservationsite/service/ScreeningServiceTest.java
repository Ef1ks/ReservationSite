package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.ScreeningRes;
import com.cinefile.reservationsite.model.Hall;
import com.cinefile.reservationsite.model.Movie;
import com.cinefile.reservationsite.model.Screening;
import com.cinefile.reservationsite.repository.HallRepository;
import com.cinefile.reservationsite.repository.MovieRepository;
import com.cinefile.reservationsite.repository.ScreeningRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private HallRepository hallRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private ScreeningService screeningService;

    private Hall hall;
    private Movie movie;

    @BeforeEach
    void setUp() {
        hall = new Hall();
        hall.setId(1L);

        movie = new Movie();
        movie.setId(new UUID(1L, 1L));
        movie.setDuration(60);
    }

    @Test
    void shouldAddScreening() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        ScreeningRes dto = new ScreeningRes(movie.getId(), hall.getId(), startTime);

        when(hallRepository.getReferenceById(hall.getId())).thenReturn(hall);
        when(movieRepository.getReferenceById(movie.getId())).thenReturn(movie);

        ArgumentCaptor<Screening> captor = ArgumentCaptor.forClass(Screening.class);

        // when
        screeningService.addScreening(dto);

        // then
        verify(screeningRepository).save(captor.capture());

        Screening saved = captor.getValue();
        assertThat(saved.getHall()).isEqualTo(hall);
        assertThat(saved.getMovie()).isEqualTo(movie);
        assertThat(saved.getStartTime()).isEqualTo(startTime);
    }

    @Test
    void shouldReturnAllScreenings() {
        // given
        Screening screening = new Screening();
        screening.setHall(hall);
        screening.setMovie(movie);
        screening.setStartTime(LocalDateTime.now());

        when(screeningRepository.findAll()).thenReturn(List.of(screening));

        // when
        List<ScreeningRes> result = screeningService.getScreenings();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).hallId()).isEqualTo(hall.getId());
        assertThat(result.get(0).movieId()).isEqualTo(movie.getId());
    }

    @Test
    void shouldReturnScreeningsBetweenDates() {
        // given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        Screening screening = new Screening();
        screening.setHall(hall);
        screening.setMovie(movie);
        screening.setStartTime(LocalDateTime.now());

        when(screeningRepository.findBystartTimeBetween(start, end))
                .thenReturn(List.of(screening));

        // when
        List<ScreeningRes> result = screeningService.getScreeningsBetween(start, end);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).hallId()).isEqualTo(hall.getId());
        assertThat(result.get(0).movieId()).isEqualTo(movie.getId());
    }

    @Test
    void shouldThrowException_whenScreeningAlreadyExists() {
        // given
        LocalDateTime start = LocalDateTime.now();

        ScreeningRes dto = new ScreeningRes(new UUID(1L, 1L), 1L, start);

        Hall hall = new Hall();
        hall.setId(1L);

        Movie movie = new Movie();
        movie.setId(new UUID(1L, 1L));
        movie.setDuration(120);

        when(hallRepository.getReferenceById(1L)).thenReturn(hall);
        when(movieRepository.getReferenceById(new UUID(1L, 1L))).thenReturn(movie);

        when(screeningRepository.existsByStartTimeAndHall_IdAndMovie_Id(
                start, 1L, new UUID(1L, 1L)
        )).thenReturn(true);

        when(screeningRepository.existsByHall_IdAndStartTimeLessThanAndEndTimeGreaterThan(
                anyLong(), any(), any()
        )).thenReturn(false);

        // when + then
        assertThrows(EntityExistsException.class,
                () -> screeningService.addScreening(dto));

        verify(screeningRepository, never()).save(any());
    }

    @Test
    void shouldThrowException_whenHallIsOccupied() {
        // given
        LocalDateTime start = LocalDateTime.now();

        ScreeningRes dto = new ScreeningRes(new UUID(1L, 1L), 1L, start);

        Hall hall = new Hall();
        hall.setId(1L);

        Movie movie = new Movie();
        movie.setId(new UUID(1L, 1L));
        movie.setDuration(120);

        when(hallRepository.getReferenceById(1L)).thenReturn(hall);
        when(movieRepository.getReferenceById(new UUID(1L, 1L))).thenReturn(movie);

        when(screeningRepository.existsByStartTimeAndHall_IdAndMovie_Id(
                start, 1L, new UUID(1L, 1L)
        )).thenReturn(false);

        when(screeningRepository.existsByHall_IdAndStartTimeLessThanAndEndTimeGreaterThan(
                eq(1L),
                any(),
                any()
        )).thenReturn(true);

        // when + then
        assertThrows(EntityExistsException.class,
                () -> screeningService.addScreening(dto));

        verify(screeningRepository, never()).save(any());
    }


}
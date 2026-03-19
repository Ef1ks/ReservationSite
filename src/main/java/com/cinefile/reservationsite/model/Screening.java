package com.cinefile.reservationsite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Screening {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Hall hall;

    private LocalDateTime startTime;
}
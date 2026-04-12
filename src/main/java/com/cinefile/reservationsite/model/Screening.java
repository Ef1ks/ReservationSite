package com.cinefile.reservationsite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
//TODO Add slugs if necessesary idk
@Entity
@Setter
@Getter
public class Screening {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Hall hall;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
package com.cinefile.reservationsite.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
//TODO Add slugs if necessesary idk
@Entity
@Setter
@Getter
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Hall hall;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
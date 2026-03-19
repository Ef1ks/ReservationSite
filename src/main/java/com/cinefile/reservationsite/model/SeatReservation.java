package com.cinefile.reservationsite.model;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class SeatReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rowNumber;
    private int columnNumber;
    @ManyToOne
    private Screening screening;

    @ManyToOne
    private User owner;
}
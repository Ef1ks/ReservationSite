package com.cinefile.reservationsite.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    int row;
    int col;

    @ManyToOne
    Screening screening;

    @ManyToOne
    User owner;

    @Column(name="owner_email",nullable = false)
    String ownerEmail;


    boolean paid;
}

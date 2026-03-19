package com.cinefile.reservationsite.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(name="password_hash" , nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

}
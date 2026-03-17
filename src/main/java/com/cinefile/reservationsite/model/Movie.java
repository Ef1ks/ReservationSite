package com.cinefile.reservationsite.model;

import jakarta.persistence.*;
import lombok.*;

import java.text.Normalizer;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "poster_url", nullable = false)
    private String posterUrl;

    @Column(nullable = false, unique = true)
    private String slug;

    @PrePersist
    @PreUpdate
    private void generateSlug() {
        if (this.title != null) {
            this.slug = slugify(this.title);
        }
    }

    // Metoda czyszcząca tytuł z polskich znaków, spacji i znaków specjalnych
    private String slugify(String input) {
        // 1. Zamiana na małe litery i zamiana spacji na myślniki
        String noWhitespace = input.trim().toLowerCase().replaceAll("\\s+", "-");

        // 2. Ręczna zamiana 'ł', ponieważ standardowy Normalizer javy ma z nim problem
        noWhitespace = noWhitespace.replace("ł", "l");

        // 3. Usunięcie znaków diakrytycznych (np. ą->a, ę->e, ó->o)
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);

        // 4. Usunięcie wszystkiego co nie jest literą, cyfrą lub myślnikiem, oraz redukcja podwójnych myślników
        return normalized.replaceAll("[^a-z0-9\\-]", "").replaceAll("-+", "-");
    }
}
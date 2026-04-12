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
    //TODO dodac to do migracji , nie zrobilem tego teraz bo bez sensu tworzcy migracje z jednym zapytaniem
    @Column(nullable = false)
    private int length;

    @Column(name = "poster_url", nullable = false)
    private String posterUrl;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "search_key", unique = true)
    private String searchKey;

    @PrePersist
    @PreUpdate
    private void generateSlug() {
        if (this.title != null) {
            this.slug = slugify(this.title);
        }
    }

    private String slugify(String input) {
        String noWhitespace = input.trim().toLowerCase().replaceAll("\\s+", "-");
        noWhitespace = noWhitespace.replace("ł", "l");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        return normalized.replaceAll("[^a-z0-9\\-]", "").replaceAll("-+", "-");
    }
}
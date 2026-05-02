package com.cinefile.reservationsite.model;

import jakarta.persistence.*;
import lombok.*;

import java.text.Normalizer;
import java.util.UUID;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int duration;

    @Column(name = "poster_url", nullable = false)
    private String posterUrl;

    @Column(name = "trailer_url")
    private String trailerUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "release_date")
    private String releaseDate;
    @Column(name = "director")
    private String director;
    @Column(name = "genre")
    private String genre;
    @Column(name = "language")
    private String originalLanguage;
    @Column(name = "imdb_rating")
    private String imdbRating;
    

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
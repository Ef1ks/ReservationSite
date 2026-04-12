package com.cinefile.reservationsite.repository;

import com.cinefile.reservationsite.model.Login.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findVerificationTokenByToken(String token);

}

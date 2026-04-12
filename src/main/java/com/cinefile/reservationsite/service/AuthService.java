package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.login.AuthResponse;
import com.cinefile.reservationsite.dto.login.LoginRequest;
import com.cinefile.reservationsite.dto.login.RegisterRequest;
import com.cinefile.reservationsite.model.Role;
import com.cinefile.reservationsite.model.User;
import com.cinefile.reservationsite.repository.UserRepository;
import com.cinefile.reservationsite.repository.VerificationTokenRepository;
import com.cinefile.reservationsite.security.UserPrincipal;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    @Transactional
    public void register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EntityExistsException("Email już zajęty.");
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);

        String tokenValue = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(tokenValue)
                .user(user)
                .expirationDate(Instant.now().plus(24, ChronoUnit.HOURS))
                .build();
        verificationTokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(email, tokenValue);

    }

    public AuthResponse login(LoginRequest req) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        var principal = (UserPrincipal) auth.getPrincipal();
        String token = jwtService.generateToken(
                principal.getId(),
                principal.getUsername(),
                principal.getRole()
        );
        return new AuthResponse(token);
    }

    @Transactional
    public void verify(VerifyEmailRequest req) {
        VerificationToken token = verificationTokenRepository.findVerificationTokenByToken(req.token())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        if (token.getExpirationDate().isBefore(Instant.now())) {
//            verificationTokenRepository.delete(token);
            throw new IllegalArgumentException("Token has expired");
        }

        User user = token.getUser();
        user.setActiveAccount(true);
//        verificationTokenRepository.delete(token);
    }
}
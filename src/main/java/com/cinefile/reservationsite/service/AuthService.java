package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.AuthResponse;
import com.cinefile.reservationsite.dto.LoginRequest;
import com.cinefile.reservationsite.dto.RegisterRequest;
import com.cinefile.reservationsite.model.User;
import com.cinefile.reservationsite.repository.UserRepository;
import com.cinefile.reservationsite.security.UserPrincipal;
import jakarta.persistence.EntityExistsException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public void register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EntityExistsException("Email już zajęty.");
        }
        var user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(req.password()))
                .build();
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest req) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        var principal = (UserPrincipal) auth.getPrincipal();
        assert principal != null;
        String token = jwtService.generateToken(principal.getId(), principal.getUsername());
        return new AuthResponse(token);
    }
}
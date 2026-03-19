package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.dto.AuthResponse;
import com.cinefile.reservationsite.dto.LoginRequest;
import com.cinefile.reservationsite.dto.RegisterRequest;
import com.cinefile.reservationsite.model.Role;
import com.cinefile.reservationsite.model.User;
import com.cinefile.reservationsite.repository.UserRepository;
import com.cinefile.reservationsite.security.UserPrincipal;
import jakarta.persistence.EntityExistsException;
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

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
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
}
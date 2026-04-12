package com.cinefile.reservationsite.controller;

import com.cinefile.reservationsite.dto.login.AuthResponse;
import com.cinefile.reservationsite.dto.login.LoginRequest;
import com.cinefile.reservationsite.dto.login.RegisterRequest;
import com.cinefile.reservationsite.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
//TODO think about login system for admins . More profesional way is to add new endpoints for admins but we can do it with frontend form
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
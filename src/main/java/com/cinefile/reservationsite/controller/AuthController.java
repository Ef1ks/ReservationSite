package com.cinefile.reservationsite.controller;

import com.cinefile.reservationsite.dto.login.LoginRequest;
import com.cinefile.reservationsite.dto.login.RegisterRequest;
import com.cinefile.reservationsite.dto.login.UserProfileDTO;
import com.cinefile.reservationsite.dto.login.VerifyEmailRequest;
import com.cinefile.reservationsite.security.UserPrincipal;
import com.cinefile.reservationsite.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//TODO think about login system for admins . More profesional way is to add new endpoints for admins but we can do it with frontend form
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDTO login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        var authResult = authService.login(req);

        ResponseCookie cookie = ResponseCookie.from("jwt", authResult.jwtToken())
                .httpOnly(true)
                .secure(false) // do zamiany
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        log.info("User logged in: {}", authResult.userProfile().email());

        return authResult.userProfile();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.FOUND)
    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false) // Na produkcji zmienisz na true
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verify(@RequestBody VerifyEmailRequest req) {
        authService.verify(req);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDTO getMe(@AuthenticationPrincipal UserPrincipal user) {
        UserProfileDTO profile = new UserProfileDTO(
                user.getId(), user.getUsername(), user.getRole().toString()
        );
        log.info("User profile requested: {}", profile.email());
        return profile;
    }
}
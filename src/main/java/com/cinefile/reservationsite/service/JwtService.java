package com.cinefile.reservationsite.service;

import com.cinefile.reservationsite.model.Login.Role;
import com.cinefile.reservationsite.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JwtService {

    private final Key key;
    private final long jwtExpirationTime;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-time}") long jwtExpirationTime
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationTime = jwtExpirationTime;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(jwtExpirationTime);

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        assert user != null;
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();


        log.info("Generating JWT token for userId: {} and email: {}", user.getId(), user.getEmail());
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parseAndValidate(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token);
    }

    public Long extractUserId(Claims claims) {
        Object uid = claims.get("userId");
        if (uid == null) {
            throw new IllegalArgumentException("Missing 'userId' claim in token");
        }
        return switch (uid) {
            case Number n -> n.longValue();
            case String s -> parseUserId(s);
            default -> throw new IllegalArgumentException("Invalid 'userId' claim type: " + uid.getClass().getName());
        };
    }

    private Long parseUserId(String userIdStr) {
        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            // Opakowujemy błąd parsowania w nasz oczekiwany typ wyjątku
            throw new IllegalArgumentException("Claim 'userId' contains invalid numeric format: " + userIdStr, e);
        }
    }

    public String extractEmail(Claims claims) {
        return claims.getSubject();
    }

    public Role extractRole(Claims claims) {
        List<?> roles = claims.get("roles", List.class);
        if (roles != null && !roles.isEmpty()) {
            String roleStr = roles.getFirst().toString();
            return Role.valueOf(roleStr);
        }
        return null;
    }
}
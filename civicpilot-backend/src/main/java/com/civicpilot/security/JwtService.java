package com.civicpilot.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.civicpilot.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    // Secret key used to sign and verify JWT
    private final SecretKey key = Jwts.SIG.HS256.key().build();

    // Token validity = 1 hour
    private final long jwtExpiration = 60 * 60 * 1000;


    // ==========================================
    // 1. GENERATE JWT TOKEN
    // ==========================================

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )
                .signWith(key)
                .compact();
    }


    // ==========================================
    // 2. EXTRACT USERNAME / EMAIL FROM JWT
    // ==========================================

    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }


    // ==========================================
    // 3. EXTRACT CLAIM FROM JWT
    // ==========================================

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }


    // ==========================================
    // 4. EXTRACT ALL CLAIMS
    // ==========================================

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    // ==========================================
    // 5. VALIDATE JWT TOKEN
    // ==========================================

    public boolean validateToken(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }


    // ==========================================
    // 6. CHECK TOKEN EXPIRATION
    // ==========================================

    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }


    // ==========================================
    // 7. GET EXPIRATION DATE
    // ==========================================

    private Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }
}
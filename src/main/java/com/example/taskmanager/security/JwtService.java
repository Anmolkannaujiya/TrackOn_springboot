package com.example.taskmanager.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    //these are congfig values defined in the properties
    //earlier we had directly hardcoded them here
    private final SecretKey key;
    private final long expiration;

    //constructor
    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration){

        //key getting value of secret
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    //token generate
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    //extract email from jwt
    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    //validate token against user
    public boolean isTokenValid(
            String token,
            UserDetails userDetails){

        String email = extractEmail(token);

        //will check against email as username in userdetail
        return email.equals(userDetails.getUsername());
    }

    public Long getExpirationSeconds() {
        return expiration / 1000;
    }
}

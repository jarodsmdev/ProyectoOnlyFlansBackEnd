package com.onlyflans.bakery.service;

import com.onlyflans.bakery.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;


    public String generateToken(final User user){
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(final User user){
        return buildToken(user,refreshExpiration);
    }

    public boolean isTokenExpired(final String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(final String token, final User user){
        final String username = extractUsername(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }

    public String buildToken(final User user, final Long expiration){
        return Jwts.builder()
                .id(user.getRut()) //user.getId().toString()
                .claims(Map.of("role", user.getUserRole())) //user.getName()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(final String token){
        return getClaims(token).getSubject();
    }

    public Date extractExpiration(final String token){
        return getClaims(token).getExpiration();
    }

    public SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

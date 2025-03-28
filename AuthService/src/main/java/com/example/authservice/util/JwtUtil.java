package com.example.authservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String generateToken(String sessionToken) {
        return Jwts.builder()
                .setSubject(sessionToken)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 1 hour
                .signWith(KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public static String extractToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
        return claims.getSubject();
    }

    public static Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

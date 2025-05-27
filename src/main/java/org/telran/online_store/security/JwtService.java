package org.telran.online_store.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telran.online_store.entity.User;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${online.store.jwt.secret.key}") String jjwtSingKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jjwtSingKey));
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        return generateToken(claims, user);
    }

    private String generateToken(Map<String, Object> claims, User user) {
        return Jwts.builder()
                .claims()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (3600 * 1000)))
                .subject(user.getEmail())
                .add(claims)
                .and()
                .signWith(secretKey)
                .compact();
    }

    public String extractUserName(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.after(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

package com.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    public String generateToken(String username) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .signWith(SignatureAlgorithm.HS256, secret)  // 移除 getBytes()
                    .compact();
        } catch (Exception e) {
            log.error("Token generation failed", e);
            throw new RuntimeException("Token generation failed: " + e.getMessage());
        }
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }
    
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)  // 移除 getBytes()
                .parseClaimsJws(token)
                .getBody();
    }
}
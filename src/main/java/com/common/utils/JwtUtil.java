package com.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

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
  /**
   * 生成token
   *
   * @param username 用户名
   * @param userId 用户ID
   * @return token字符串
   */
  public String generateToken(String username, Long userId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration * 1000);

    return Jwts.builder()
        .setSubject(username)
        .claim("userId", userId.toString())
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }
  /** 从token中获取用户ID */
  public Long getUserIdFromToken(String token) {
    try {
      Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

      // 假设在生成token时已经将userId放入claims中
      return Long.parseLong(claims.get("userId", String.class));
    } catch (Exception e) {
      return null;
    }
  }

  private Claims getClaimsFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(secret) // 移除 getBytes()
        .parseClaimsJws(token)
        .getBody();
  }
}

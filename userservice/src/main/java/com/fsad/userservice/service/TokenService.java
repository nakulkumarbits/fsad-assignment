package com.fsad.userservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {
  public static final String ISSUER = "USERSERVICE";
  public static final String SECRET_KEY = "5313ea05b866ef3922983e3d0f0eee22698e0788f676b3bfa7bc48eb2561e467";
  private Map<String, String> tokenStore = new HashMap<>();

  public void storeToken(String key, String token) {
    tokenStore.put(key, token);
  }

  public String tokenExistsForKey(String key) {
    return tokenStore.get(key);
  }

  public void invalidate(String token) {
    try {
      Claims payload = getPayload(token);
      String key = (String) payload.get("username");
      tokenStore.remove(key);
    } catch (ExpiredJwtException ignored) {
      // nothing to be done.
    }
  }

  public String createJWT(String userName, String email) {
    Date issuedAt = Date.from(Instant.now(Clock.system(ZoneId.systemDefault())));
    Date expiry = Date.from(issuedAt.toInstant().plus(2L, ChronoUnit.MINUTES));
    String id = UUID.randomUUID().toString().replace("-", "");

    Map<String, String> claims = new HashMap<>();
    claims.put("username", userName);
    claims.put("email", email);

    return Jwts.builder()
        .claims(claims)
        .issuer(ISSUER)
        .id(id)
        .issuedAt(issuedAt)
        .expiration(expiry)
        .signWith(getSecretKey())
        .compact();
  }

  public boolean validate(String token) {
    try {
      Claims payload = getPayload(token);
      String username = (String) payload.get("username");
      if (tokenExistsForKey(username) == null) {
        return false;
      }
      Date expiration = payload.getExpiration();
      return !expiration.before(Date.from(Instant.now()));
    } catch (ExpiredJwtException ex) {
      return false;
    }
  }

  private SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }

  private Claims getPayload(String token) {
    Jws<Claims> claims = Jwts.parser()
        .verifyWith(getSecretKey())
        .build()
        .parseSignedClaims(token);
    return claims.getPayload();
  }
}

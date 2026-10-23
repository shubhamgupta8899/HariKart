package com.hariom.hariKart.security;

import com.hariom.hariKart.service.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String TYPE_CLAIM = "type";
    public static final String ACCESS = "access";
    public static final String REFRESH = "refresh";

    private final SecretKey signingKey;
    private final long accessExpiryMs;
    private final long refreshExpiryMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access-token-expiry-ms}") long accessExpiryMs,
                   @Value("${jwt.refresh-token-expiry-ms}") long refreshExpiryMs) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiryMs = accessExpiryMs;
        this.refreshExpiryMs = refreshExpiryMs;
    }

    public long getAccessExpiryMs() { return accessExpiryMs; }

    public String generateAccessToken(CustomUserDetails p) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", p.getUserId());
        claims.put("role", p.getAuthorities().iterator().next().getAuthority());
        claims.put(TYPE_CLAIM, ACCESS);
        if (p.getRouteAssigned() != null) {
            claims.put("route", p.getRouteAssigned());
        }
        return buildToken(p, accessExpiryMs, claims);
    }

    public String generateRefreshToken(CustomUserDetails p) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", p.getUserId());
        claims.put(TYPE_CLAIM, REFRESH);
        return buildToken(p, refreshExpiryMs, claims);
    }

    private String buildToken(CustomUserDetails p, long ttl, Map<String, Object> claims) {
        Date now = new Date();
        return Jwts.builder()
                .claims(claims)
                .subject(p.getUsername())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ttl))
                .signWith(signingKey)
                .compact();
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return parse(token).getSubject();
    }

    /** Valid = signature OK, not expired, correct subject, correct token type. */
    public boolean isValid(String token, CustomUserDetails principal, String expectedType) {
        try {
            Claims c = parse(token);
            return c.getSubject().equals(principal.getUsername())
                    && expectedType.equals(c.get(TYPE_CLAIM, String.class));
        } catch (Exception ex) {
            return false;
        }
    }
}
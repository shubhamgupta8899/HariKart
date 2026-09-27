package com.hariom.hariKart.security;

import com.hariom.hariKart.service.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtUtil {

    private final SecretKey signingKey;
    private final long accessExpiryMs;
    private final long refreshExpiryMs;

    public JwtUtil(
            @Value("$jwt.secret") String secret,
            @Value("$jwt.access-token-expiry-ms") long accessExpiryMs,
            @Value("$jwt.refresh-token-expiry-ms") long refreshExpiryMs) {

        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessExpiryMs = accessExpiryMs;
        this.refreshExpiryMs = refreshExpiryMs;
    }

    public String generateAcessToken(CustomUserDetails principal) {

        return buildToken(principal, accessExpiryMs, Map.of(

                "uid", principal.getUserId(),
                "role", principal.getAuthorities().iterator().next().getAuthority(),
                "route", principal.getRouteAssigned()
        ));
    }

    public String generateRefreshToken(CustomUserDetails principal){

        return buildToken(principal, refreshExpiryMs, Map.of(
                "uid", principal.getUserId(),
                "fyp", "refresh"
        ));
    }

    private String buildToken(CustomUserDetails principal, long ttl, Map<String, Object> claims) {

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(principal.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ ttl))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}

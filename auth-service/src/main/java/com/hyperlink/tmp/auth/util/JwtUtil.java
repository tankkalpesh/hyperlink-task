package com.hyperlink.tmp.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    public String generateToken(String subject, String role) {
        Algorithm algo = Algorithm.HMAC256(jwtSecret.getBytes());
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);
        return JWT.create()
                .withSubject(subject)
                .withClaim("role", role)
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(algo);
    }

    public String validateAndGetSubject(String token) {
        Algorithm algo = Algorithm.HMAC256(jwtSecret.getBytes());
        return JWT.require(algo).build().verify(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        Algorithm algo = Algorithm.HMAC256(jwtSecret.getBytes());
        return JWT.require(algo).build().verify(token).getClaim("role").asString();
    }
}

package com.osh4.accounting.config.security;

import com.osh4.accounting.dto.UserLoginResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
public class JwtTokenAuthenticatorService {

    private final KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();

    public String createJwt(UserLoginResponseDto user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuer("identity")
                .claim("roles", user.getRoles())
                .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(15))))
                .issuedAt(Date.from(Instant.now()))
                .signWith(keyPair.getPrivate(), Jwts.SIG.RS256)
                .compact();
    }

    public Jws<Claims> validateJwt(String jwt) {
        return Jwts.parser().verifyWith(keyPair.getPublic()).build().parseSignedClaims(jwt);
    }
}

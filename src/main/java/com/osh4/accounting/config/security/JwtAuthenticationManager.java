package com.osh4.accounting.config.security;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtTokenAuthenticatorService jwtTokenAuthenticatorService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(Authentication::getCredentials)
                .map(String.class::cast)
                .map(jwtTokenAuthenticatorService::validateJwt)
                .onErrorResume(it -> Mono.empty())
                .map(jws -> new UsernamePasswordAuthenticationToken(
                        jws.getPayload().getSubject(),
                        authentication.getCredentials(),
                        Optional.ofNullable(jws.getPayload().get("roles"))
                                .filter(List.class::isInstance)
                                .map(roles -> (List<?>) roles)
                                .orElse(Collections.emptyList())
                                .stream()
                                .filter(x -> x instanceof LinkedHashMap)
                                .map(LinkedHashMap.class::cast)
                                .map(x -> x.get("authority"))
                                .map(String.class::cast)
                                .map(SimpleGrantedAuthority::new)
                                .toList())
                );
    }
}


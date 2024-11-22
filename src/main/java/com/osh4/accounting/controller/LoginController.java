package com.osh4.accounting.controller;

import com.osh4.accounting.config.security.JwtTokenAuthenticatorService;
import com.osh4.accounting.dto.UserCredentialsDto;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class LoginController extends BaseController {
    private final JwtTokenAuthenticatorService jwtTokenAuthenticatorService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Mono<ResponseEntity<Void>> login(@Valid @RequestBody UserCredentialsDto user) {
        return Mono.justOrEmpty(user)
                .map(UserCredentialsDto::getEmail)
                .flatMap(userService::findByUsername)
                .filter(Objects::nonNull)
                .filter(dbUser -> passwordEncoder.matches(user.getPassword(), dbUser.getPassword()))
                .map(jwtTokenAuthenticatorService::createJwt)
                .map(LoginController::buildAuthCookie)
                .map(LoginController::buildResponseWithAuthCookie)
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    private static ResponseEntity<Void> buildResponseWithAuthCookie(ResponseCookie authCookie) {
        return ResponseEntity.noContent()
                .header("Set-Cookie", authCookie.toString())
                .build();
    }

    private static ResponseCookie buildAuthCookie(String jwt) {
        return ResponseCookie.fromClientResponse("X-Auth", jwt)
                .maxAge(900)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .build();
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<UserDto>> signup(@Valid @RequestBody UserCredentialsDto dto) {
        return userService.signUp(dto)
                .flatMap(this::successResponse);
    }
}

package com.osh4.accounting.controller.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osh4.accounting.exception.AlreadyExistsException;
import com.osh4.accounting.exception.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;


import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@RequiredArgsConstructor
public class ValidationHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable throwable) {

        ServerHttpResponse response = exchange.getResponse();
        if (throwable instanceof WebExchangeBindException validationEx) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return writeResponse(exchange, objectMapper.writeValueAsBytes(getValidationErrors(validationEx)));
        } else if (throwable instanceof NotFoundException || throwable instanceof AlreadyExistsException) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return writeResponse(exchange, objectMapper.writeValueAsBytes(throwable.getMessage()));
        } else if (throwable instanceof ExpiredJwtException || throwable instanceof SignatureException) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.getCookies().remove("X-Auth");
            return writeResponse(exchange, objectMapper.writeValueAsBytes(throwable.getMessage()));
        } else {
            return Mono.error(throwable);
        }
    }

    private Map<String, String> getValidationErrors(final WebExchangeBindException validationEx) {

        return validationEx.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField,
                error -> Optional.ofNullable(error.getDefaultMessage()).orElse("")));
    }

    private Mono<Void> writeResponse(final ServerWebExchange exchange, final byte[] responseBytes) {

        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBytes)));
    }
}

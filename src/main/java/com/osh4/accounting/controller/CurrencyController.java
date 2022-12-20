package com.osh4.accounting.controller;

import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.repository.CurrencyRepository;
import com.osh4.accounting.service.CurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/currency")
@Slf4j
@AllArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public Flux<CurrencyDto> getAll() {
        return currencyService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<CurrencyDto> get(@PathVariable String id) {
        return currencyService.get(id);
    }

    @PostMapping
    public Mono<ResponseEntity<String>> create(@RequestBody CurrencyDto currencyDto) {
        return currencyService.create(currencyDto)
                .flatMap(s -> Mono.just(ResponseEntity.accepted().contentType(MediaType.APPLICATION_JSON).body(s)))
                .onErrorReturn(ResponseEntity.unprocessableEntity().body("Can't add the currency"));
    }

    @PutMapping
    public Mono<ResponseEntity<String>> update(@RequestBody CurrencyDto currencyDto) {
        return currencyService.update(currencyDto)
                .flatMap(s -> Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(s)))
                .onErrorReturn(ResponseEntity.unprocessableEntity().body("Can't update the currency"));
    }

    @DeleteMapping
    public Mono<ResponseEntity<String>> delete(@RequestBody CurrencyDto currencyDto) {
        return currencyService.delete(currencyDto)
                .flatMap(s -> Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(s)))
                .onErrorReturn(ResponseEntity.unprocessableEntity().body("Can't remove the currency"));
    }
}

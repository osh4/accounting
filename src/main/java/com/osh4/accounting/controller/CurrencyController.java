package com.osh4.accounting.controller;

import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.service.CurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/currency")
@Slf4j
@AllArgsConstructor
public class CurrencyController extends BaseController {

    private final CurrencyService currencyService;

    @GetMapping
    public Mono<Page<CurrencyDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return currencyService.getAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CurrencyDto>> get(@PathVariable String id) {
        return currencyService.get(id)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @PostMapping
    public Mono<ResponseEntity<String>> create(@RequestBody CurrencyDto currencyDto) {
        return currencyService.create(currencyDto)
                .flatMap(s -> successResponseCreate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseCreate());
    }

    @PutMapping
    public Mono<ResponseEntity<String>> update(@RequestBody CurrencyDto currencyDto) {
        return currencyService.update(currencyDto)
                .flatMap(s -> successResponseUpdate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseUpdate());
    }

    @DeleteMapping
    public Mono<ResponseEntity<String>> delete(@RequestBody CurrencyDto currencyDto) {
        return currencyService.delete(currencyDto)
                .flatMap(s -> successResponseDelete())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseDelete());
    }
}

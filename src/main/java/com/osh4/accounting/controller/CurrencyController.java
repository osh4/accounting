package com.osh4.accounting.controller;

import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.service.CurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id_asc") String sort) {
        return currencyService.getAll(paginatedSearchService.paginationInfo(page, size, sort));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CurrencyDto>> get(@PathVariable String id) {
        return currencyService.get(id)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @GetMapping("/isocode/{isocode}")
    public Mono<ResponseEntity<CurrencyDto>> getByIsocode(@PathVariable String isocode) {
        return currencyService.getByIsocode(isocode)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @PostMapping
    public Mono<ResponseEntity<CurrencyDto>> create(@RequestBody CurrencyDto dto) {
        return currencyService.create(dto)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CurrencyDto>> update(@PathVariable String id, @RequestBody CurrencyDto dto) {
        return currencyService.update(id, dto)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return currencyService.delete(id)
                .flatMap(s -> successResponseDelete())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseDelete());
    }
}

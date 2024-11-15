package com.osh4.accounting.controller;

import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
@Slf4j
@AllArgsConstructor
public class AccountController extends BaseController {
    private final AccountService accountService;

    @GetMapping
    public Mono<Page<AccountDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id_asc") String sort) {
        return accountService.getAll(paginatedSearchService.paginationInfo(page, size, sort));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountDto>> get(@PathVariable String id) {
        return accountService.get(id)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @PostMapping
    public Mono<ResponseEntity<AccountDto>> create(@RequestBody AccountDto dto) {
        return accountService.create(dto)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<AccountDto>> update(@PathVariable String id, @RequestBody AccountDto dto) {
        return accountService.update(id, dto)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return accountService.delete(id)
                .flatMap(s -> successResponseDelete())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseDelete());
    }
}

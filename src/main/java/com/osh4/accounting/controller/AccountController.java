package com.osh4.accounting.controller;

import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
                                         @RequestParam(defaultValue = "10") int size) {
        return accountService.getAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountDto>> get(@PathVariable String id) {
        return accountService.get(id)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @PostMapping
    public Mono<ResponseEntity<String>> create(@RequestBody AccountDto accountDto) {
        return accountService.create(accountDto)
                .flatMap(s -> successResponseCreate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseCreate());
    }

    @PutMapping
    public Mono<ResponseEntity<String>> update(@RequestBody AccountDto accountDto) {
        return accountService.update(accountDto)
                .flatMap(s -> successResponseUpdate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseUpdate());
    }

    @DeleteMapping
    public Mono<ResponseEntity<String>> delete(@RequestBody AccountDto accountDto) {
        return accountService.delete(accountDto)
                .flatMap(s -> successResponseDelete())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseDelete());
    }
}

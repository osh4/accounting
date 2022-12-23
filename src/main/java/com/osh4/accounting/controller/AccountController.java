package com.osh4.accounting.controller;

import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
@Slf4j
@AllArgsConstructor
public class AccountController extends BaseController {
    private final AccountService accountService;

    @GetMapping
    public Flux<AccountDto> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<AccountDto> get(@PathVariable String id) {
        return accountService.get(id);
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

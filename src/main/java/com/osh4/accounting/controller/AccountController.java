package com.osh4.accounting.controller;

import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
@Slf4j
@AllArgsConstructor
public class AccountController {

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
                .flatMap(s -> Mono.just(ResponseEntity.accepted().contentType(MediaType.APPLICATION_JSON).body(s)))
                .onErrorReturn(ResponseEntity.unprocessableEntity().body("Can't add the account"));
    }

    @PutMapping
    public Mono<ResponseEntity<String>> update(@RequestBody AccountDto accountDto) {
        return accountService.update(accountDto)
                .flatMap(s -> Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(s)))
                .onErrorReturn(ResponseEntity.unprocessableEntity().body("Can't update the account"));
    }

    @DeleteMapping
    public Mono<ResponseEntity<String>> delete(@RequestBody AccountDto accountDto) {
        return accountService.delete(accountDto)
                .flatMap(s -> Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(s)))
                .onErrorReturn(ResponseEntity.unprocessableEntity().body("Can't remove the account"));
    }
}

package com.osh4.accounting.service;

import com.osh4.accounting.dto.AccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Flux<AccountDto> getAll();

    Mono<AccountDto> get(String id);

    Mono<String> create(AccountDto dto);

    Mono<String> update(AccountDto dto);

    Mono<String> delete(AccountDto dto);
}

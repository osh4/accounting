package com.osh4.accounting.service;

import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Flux<AccountDto> getAll();

    Mono<AccountDto> get(String id);

    Mono<Account> create(AccountDto dto);

    Mono<Void> update(AccountDto dto);

    Mono<Void> delete(AccountDto dto);
}

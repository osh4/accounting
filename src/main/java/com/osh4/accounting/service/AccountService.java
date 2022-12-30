package com.osh4.accounting.service;

import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<Page<AccountDto>> getAll(PageRequest pageRequest);

    Mono<AccountDto> get(String id);

    Mono<Account> create(AccountDto dto);

    Mono<Void> update(AccountDto dto);

    Mono<Void> delete(AccountDto dto);
}

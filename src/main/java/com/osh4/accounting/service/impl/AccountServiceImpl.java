package com.osh4.accounting.service.impl;

import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import com.osh4.accounting.persistance.repository.AccountRepository;
import com.osh4.accounting.service.AccountService;
import com.osh4.accounting.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private CurrencyService currencyService;

    @Override
    public Flux<AccountDto> getAll() {
        return accountRepository.findAll()
                .map(this::toDto);
    }

    @Override
    public Mono<AccountDto> get(String id) {
        return accountRepository.findById(id)
                .map(this::toDto);
    }

    @Override
    public Mono<String> create(AccountDto dto) {
        return accountRepository.findById(dto.getId())
                .flatMap(x -> Mono.error(new DuplicateKeyException("AlreadyExist")))
                .switchIfEmpty(toModel(dto).flatMap(accountRepository::save))
                .flatMap(x -> Mono.just("Add Success"));
    }


    @Override
    public Mono<String> update(AccountDto dto) {
        return accountRepository.findById(dto.getId())
                .map(acc -> setValues(acc, dto))
                .flatMap(x -> Mono.just("Successful update!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with update")));
    }


    @Override
    public Mono<String> delete(AccountDto dto) {
        return accountRepository.findById(dto.getId())
                .map(accountRepository::delete)
                .flatMap(x -> Mono.just("Successful remove!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with removal")));
    }

    private Account setValues(Account acc, AccountDto dto) {
        if (nonNull(dto.getName()) && ObjectUtils.notEqual(acc.getName(), dto.getName())) {
            acc.setName(dto.getName());
        }
        if (nonNull(dto.getDescription()) && ObjectUtils.notEqual(acc.getDescription(), dto.getDescription())) {
            acc.setDescription(dto.getDescription());
        }
        if (nonNull(dto.getCurrency()) && ObjectUtils.notEqual(acc.getCurrencyId(), dto.getCurrency())) {
            acc.setCurrencyId(dto.getCurrency().getId());
        }
        return acc;
    }

    private Mono<Account> toModel(AccountDto dto) {
        return Mono.just(Account.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .currencyId(dto.getCurrency().getId())
                .userId(dto.getUserId())
                .build());
    }

    private AccountDto toDto(Account acc) {
        return AccountDto.builder()
                .id(acc.getId())
                .name(acc.getName())
                .description(acc.getDescription())
                .currency(getCurrency(acc.getCurrencyId()))
                .build();
    }

    private CurrencyDto getCurrency(String currencyId) {
        if (StringUtils.isBlank(currencyId)) {
            return null;
        }
        return currencyService.get(currencyId).block();
    }
}

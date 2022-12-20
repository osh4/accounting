package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import com.osh4.accounting.persistance.repository.AccountRepository;
import com.osh4.accounting.service.AccountService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private Converter<Account, AccountDto> accountConverter;
    private Converter<AccountDto, Mono<Account>> accountReverseConverter;

    @Override
    public Flux<AccountDto> getAll() {
        return accountRepository.findAll()
                .map(accountConverter::convert);
    }

    @Override
    public Mono<AccountDto> get(String id) {
        return accountRepository.findById(id)
                .map(accountConverter::convert);
    }

    @Override
    public Mono<String> create(AccountDto dto) {
        return accountRepository.findById(dto.getId())
                .flatMap(x -> Mono.error(new DuplicateKeyException("AlreadyExist")))
                .switchIfEmpty(accountReverseConverter.convert(dto).flatMap(accountRepository::save))
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
}

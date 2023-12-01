package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import com.osh4.accounting.persistance.repository.AccountRepository;
import com.osh4.accounting.service.AccountService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private Converter<Account, AccountDto> accountConverter;
    private Converter<AccountDto, Account> accountReverseConverter;

    @Override
    public Mono<Page<AccountDto>> getAll(PageRequest pageRequest) {
        return accountRepository.findAllBy(pageRequest)
                .map(accountConverter::convert)
                .collectList()
                .zipWith(accountRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    @Override
    public Mono<AccountDto> get(String id) {
        return accountRepository.findById(id)
                .map(accountConverter::convert)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<Account> create(AccountDto dto) {
        return accountRepository.save(accountReverseConverter.convert(dto).setAsNew());
    }

    @Override
    @Transactional
    public Mono<Void> update(String id, AccountDto dto) {
        return accountRepository.findById(id)
                .flatMap(model -> updateFields(model, dto))
                .then();
    }

    @Override
    public Mono<Void> delete(String id) {
        return accountRepository.deleteById(id);
    }

    private Mono<Account> updateFields(Account model, AccountDto dto) {
        if (nonNull(dto.getName()) && ObjectUtils.notEqual(model.getName(), dto.getName())) {
            model.setName(dto.getName());
        }
        if (nonNull(dto.getDescription()) && ObjectUtils.notEqual(model.getDescription(), dto.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        if (nonNull(dto.getCurrency()) && ObjectUtils.notEqual(model.getCurrencyId(), dto.getCurrency())) {
            model.setCurrencyId(dto.getCurrency().getId());
        }
        return accountRepository.save(model);
    }
}

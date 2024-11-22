package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.AccountMapper;
import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.exception.AlreadyExistsException;
import com.osh4.accounting.exception.NotFoundException;
import com.osh4.accounting.persistance.r2dbc.Account;
import com.osh4.accounting.persistance.repository.AccountRepository;
import com.osh4.accounting.service.AccountService;
import com.osh4.accounting.service.CurrencyService;
import com.osh4.accounting.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Slf4j
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private CurrencyService currencyService;
    private UserService userService;
    private AccountMapper accountMapper;

    @Override
    public Mono<Page<AccountDto>> getAll(PageRequest pageRequest) {
        return accountRepository.findAllBy(pageRequest)
                .map(accountMapper::toDto)
                .flatMap(this::populateUser)
                .flatMap(this::populateCurrency)
                .collectList()
                .zipWith(accountRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    private Mono<AccountDto> populateUser(AccountDto dto) {
        return Mono.justOrEmpty(dto.getUser())
                .filter(Objects::nonNull)
                .map(UserDto::getId)
                .filter(StringUtils::isNotBlank)
                .flatMap(userService::get)
                .map(user -> {
                    dto.setUser(user);
                    return dto;
                })
                .switchIfEmpty(Mono.just(dto))
                .onErrorReturn(dto);
    }

    private Mono<AccountDto> populateCurrency(AccountDto dto) {
        return Mono.justOrEmpty(dto.getCurrency())
                .filter(Objects::nonNull)
                .map(CurrencyDto::getId)
                .filter(StringUtils::isNotBlank)
                .flatMap(currencyService::get)
                .map(currency -> {
                    dto.setCurrency(currency);
                    return dto;
                })
                .switchIfEmpty(Mono.just(dto))
                .onErrorReturn(dto);
    }

    @Override
    public Mono<AccountDto> get(String id) {
        return accountRepository.findById(id)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorResume(it -> Mono.empty())
                .map(accountMapper::toDto)
                .switchIfEmpty(Mono.error(NotFoundException.fromAccountId(id)));
    }

    @Override
    public Mono<AccountDto> create(AccountDto dto) {
        return accountRepository.findByName(dto.getName())
                .switchIfEmpty(Mono.just(accountMapper.toModel(dto).setAsNew()).flatMap(accountRepository::save))
                .filter(Account::isNewEntity)
                .map(accountMapper::toDto)
                .switchIfEmpty(Mono.error(AlreadyExistsException.fromAccountName(dto.getName())));
    }

    @Override
    @Transactional
    public Mono<AccountDto> update(String id, AccountDto dto) {
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.fromAccountId(id)))
                .flatMap(model -> updateFields(model, dto))
                .map(accountMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return accountRepository.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.fromAccountId(id)))
                .flatMap(account -> accountRepository.deleteById(id));
    }

    @Override
    public Mono<TransactionDto> recalculateAccountAmount(TransactionDto dto) {
        AccountDto sourceAccount = dto.getSourceAccount();
        AccountDto targetAccount = dto.getTargetAccount();
        BigDecimal amount = dto.getAmount();
        synchronized (this) {
            sourceAccount.setAmount(sourceAccount.getAmount().add(amount.negate()));
            targetAccount.setAmount(sourceAccount.getAmount().add(amount));
            AccountDto newSourceAccount = this.update(sourceAccount.getId(), sourceAccount).block();
            AccountDto newTargetAccount = this.update(targetAccount.getId(), targetAccount).block();
            dto.setSourceAccount(newSourceAccount);
            dto.setTargetAccount(newTargetAccount);
        }

        return Mono.just(dto);
    }

    private Mono<Account> updateFields(Account model, AccountDto dto) {
        if (isNull(dto)) {
            return Mono.just(model);
        }
        if (isNotBlank(dto.getName()) && ObjectUtils.notEqual(model.getName(), dto.getName())) {
            model.setName(dto.getName());
        }
        if (isNotBlank(dto.getDescription()) && ObjectUtils.notEqual(model.getDescription(), dto.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        if (nonNull(dto.getCurrency()) && isNotBlank(dto.getCurrency().getId()) && isNotBlank(dto.getCurrency().getId()) && ObjectUtils.notEqual(model.getCurrencyId(), dto.getCurrency().getId())) {
            model.setCurrencyId(dto.getCurrency().getId());
        }
        if (nonNull(dto.getUser()) && isNotBlank(dto.getUser().getId()) && isNotBlank(dto.getUser().getId()) && ObjectUtils.notEqual(model.getUserId(), dto.getUser().getId())) {
            model.setUserId(dto.getUser().getId());
        }
        return accountRepository.save(model);
    }
}

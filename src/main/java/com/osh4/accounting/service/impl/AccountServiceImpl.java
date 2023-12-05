package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.AccountMapper;
import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import com.osh4.accounting.persistance.repository.AccountRepository;
import com.osh4.accounting.persistance.repository.CurrencyRepository;
import com.osh4.accounting.persistance.repository.UserRepository;
import com.osh4.accounting.service.AccountService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private CurrencyRepository currencyRepository;
    private UserRepository userRepository;
    private AccountMapper accountMapper;

    @Override
    public Mono<Page<AccountDto>> getAll(PageRequest pageRequest) {
        return accountRepository.findAllBy(pageRequest)
                .flatMap(this::populateCurrency)
                .flatMap(this::populateUser)
                .map(accountMapper::toDto)
                .collectList()
                .zipWith(accountRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    private Publisher<? extends Account> populateUser(Account account) {
        return userRepository.findById(account.getUserId())
                .map(user -> {
                    account.setUser(user);
                    return account;
                });
    }

    private Publisher<? extends Account> populateCurrency(Account account) {
        return currencyRepository.findById(account.getCurrencyId())
                .map(cur -> {
                    account.setCurrency(cur);
                    return account;
                });
    }

    @Override
    public Mono<AccountDto> get(String id) {
        return accountRepository.findById(id)
                .map(accountMapper::toDto)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<Account> create(AccountDto dto) {
        return accountRepository.save(accountMapper.toModel(dto).setAsNew());
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
        return accountRepository.save(model);
    }
}

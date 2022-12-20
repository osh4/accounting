package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
public class AccountReverseConverter implements Converter<AccountDto, Mono<Account>> {

    @Override
    public Mono<Account> convert(AccountDto dto) {
        return Mono.just(Account.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .currencyId(dto.getCurrency().getId())
                .userId(dto.getUserId())
                .build());
    }
}

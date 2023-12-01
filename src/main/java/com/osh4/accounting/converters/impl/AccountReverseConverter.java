package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import org.springframework.stereotype.Component;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
public class AccountReverseConverter implements Converter<AccountDto, Account> {

    @Override
    public Account convertInternal(AccountDto dto) {
        return Account.builder()
                .id(createIdIfNeeded(dto.getId()))
                .name(dto.getName())
                .description(dto.getDescription())
                .currencyId(dto.getCurrency().getId())
                .userId(dto.getUser().getId())
                .build();
    }
}

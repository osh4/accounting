package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
public class AccountReverseConverter implements Converter<AccountDto, Account> {

    @Override
    public Account convert(AccountDto dto) {
        return isNull(dto) ? null : Account.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .currencyId(dto.getCurrency().getId())
                .userId(dto.getUserId())
                .build();
    }
}

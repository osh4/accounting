package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import com.osh4.accounting.service.CurrencyService;
import com.osh4.accounting.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
@AllArgsConstructor
public class AccountConverter implements Converter<Account, AccountDto> {
    private CurrencyService currencyService;
    private UserService userService;

    @Override
    public AccountDto convert(Account model) {
        return isNull(model) ? null : AccountDto.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .currency(getCurrency(model.getCurrencyId()))
                .user(userService.get(model.getUserId()).block())
                .build();
    }

    private CurrencyDto getCurrency(String currencyId) {
        if (StringUtils.isBlank(currencyId)) {
            return null;
        }
        return currencyService.get(currencyId).block();
    }
}

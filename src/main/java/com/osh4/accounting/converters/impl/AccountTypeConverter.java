package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.account.AccountTypeDto;
import com.osh4.accounting.persistance.entity.account.AccountType;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static java.util.Objects.isNull;

@Service
public class AccountTypeConverter implements Converter<AccountType, AccountTypeDto> {
    @Override
    public AccountTypeDto convert(AccountType accountType) {
        AccountTypeDto result = new AccountTypeDto();
        if (isNull(accountType)) {
            return result;
        }

        Locale currentLocale = LocaleContextHolder.getLocale();
        result.setName(accountType.getName().get(currentLocale));

        return result;
    }
}

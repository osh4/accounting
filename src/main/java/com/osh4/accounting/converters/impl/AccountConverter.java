package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.account.AccountDto;
import com.osh4.accounting.persistance.entity.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AccountConverter implements Converter<Account, AccountDto> {
    private final AccountTypeConverter accountTypeConverter;

    @Autowired
    public AccountConverter(AccountTypeConverter accountTypeConverter) {
        this.accountTypeConverter = accountTypeConverter;
    }

    @Override
    public AccountDto convert(Account account) {
        AccountDto result = new AccountDto();
        Locale currentLocale = LocaleContextHolder.getLocale();
        result.setName(account.getName().get(currentLocale));
        result.setType(accountTypeConverter.convert(account.getAccountType()));
        //result.setDescription(account.getDescription());
        return result;
    }
}

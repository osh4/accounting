package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.r2dbc.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring", uses = {CurrencyMapper.class, UserMapper.class})
public interface AccountMapper {
    AccountDto toDto(Account model);

    @Mapping(target = "isNewEntity", ignore = true)
    @Mapping(source = "currency", target = "currencyId", qualifiedByName = "currencySaveMapper")
    @Mapping(source = "user", target = "userId", qualifiedByName = "userSaveMapper")
    Account toModel(AccountDto dto);

    @Named("currencySaveMapper")
    static String currencySaveMapper(CurrencyDto currencyDto) {
        return Optional.ofNullable(currencyDto).map(CurrencyDto::getId).orElse(null);
    }

    @Named("userSaveMapper")
    static String userSaveMapper(UserDto userDto) {
        return Optional.ofNullable(userDto).map(UserDto::getId).orElse(null);
    }
}

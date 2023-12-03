package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;
import java.util.UUID;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring", uses = {TransactionTypeMapper.class, AccountMapper.class})
public interface TransactionMapper {
    TransactionDto toDto(Transaction model);

    @Mapping(source = "transactionType", target = "transactionTypeId", qualifiedByName = "transactionTypeSaveMapper")
    @Mapping(source = "sourceAccount", target = "sourceAccountId", qualifiedByName = "accountSaveMapper")
    @Mapping(source = "targetAccount", target = "targetAccountId", qualifiedByName = "accountSaveMapper")
    @Mapping(source = "id", target = "id", qualifiedByName = "idMapper")
    Transaction toModel(TransactionDto dto);

    @Named("transactionTypeSaveMapper")
    static String transactionTypeSaveMapper(TransactionTypeDto transactionTypeDto) {
        return transactionTypeDto.getId();
    }

    @Named("accountSaveMapper")
    static String AccountSaveMapper(AccountDto accountDto) {
        return accountDto.getId();
    }

    @Named("idMapper")
    static String idMapper(String id) {
        return Optional.ofNullable(id).orElse(UUID.randomUUID().toString());
    }
}

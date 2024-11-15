package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.*;
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
    @Mapping(source = "transactionCategoryId", target = "transactionCategory", qualifiedByName = "transactionCategoryGetMapper")
    TransactionDto toDto(Transaction model);

    @Mapping(source = "transactionType", target = "transactionTypeId", qualifiedByName = "transactionTypeSaveMapper")
    @Mapping(source = "transactionCategory", target = "transactionCategoryId", qualifiedByName = "transactionCategorySaveMapper")
    @Mapping(source = "sourceAccount", target = "sourceAccountId", qualifiedByName = "accountSaveMapper")
    @Mapping(source = "targetAccount", target = "targetAccountId", qualifiedByName = "accountSaveMapper")
    @Mapping(source = "id", target = "id", qualifiedByName = "idMapper")
    Transaction toModel(TransactionDto dto);

    @Named("transactionTypeSaveMapper")
    static String transactionTypeSaveMapper(TransactionTypeDto transactionTypeDto) {
        return transactionTypeDto.getId();
    }

    @Named("transactionCategorySaveMapper")
    static String transactionCategorySaveMapper(TransactionCategoryDto transactionCategoryDto) {
        return Optional.ofNullable(transactionCategoryDto).map(TransactionCategoryDto::getId).orElse(null);
    }

    @Named("accountSaveMapper")
    static String accountSaveMapper(AccountDto accountDto) {
        return accountDto.getId();
    }

    @Named("idMapper")
    static String idMapper(String id) {
        return Optional.ofNullable(id).orElse(UUID.randomUUID().toString());
    }

    @Named("transactionCategoryGetMapper")
    static TransactionCategoryDto settingTypeGetMapper(String transactionCategoryId) {
        return Optional.ofNullable(transactionCategoryId)
                .map(categoryId -> TransactionCategoryDto.builder().id(categoryId).build())
                .orElse(null);
    }
}

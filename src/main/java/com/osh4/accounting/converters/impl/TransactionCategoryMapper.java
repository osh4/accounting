package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.TransactionCategoryDto;
import com.osh4.accounting.persistance.r2dbc.TransactionCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;
import java.util.UUID;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface TransactionCategoryMapper {
    TransactionCategoryDto toDto(TransactionCategory model);

    @Mapping(target = "isNewEntity", ignore = true)
    @Mapping(source = "id", target = "id", qualifiedByName = "transactionCategoryIdSaveMapper")
    TransactionCategory toModel(TransactionCategoryDto dto);

    @Named("transactionCategoryIdSaveMapper")
    static String transactionCategoryIdSaveMapper(String id) {
        return Optional.ofNullable(id).orElse(UUID.randomUUID().toString());
    }
}

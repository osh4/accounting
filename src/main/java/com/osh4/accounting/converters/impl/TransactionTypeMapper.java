package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.persistance.r2dbc.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;
import java.util.UUID;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface TransactionTypeMapper {
    TransactionTypeDto toDto(TransactionType model);

    @Mapping(target = "isNewEntity", ignore = true)
    @Mapping(source = "id", target = "id", qualifiedByName = "transactionTypeIdSaveMapper")
    TransactionType toModel(TransactionTypeDto dto);

    @Named("transactionTypeIdSaveMapper")
    static String transactionTypeIdSaveMapper(String id) {
        return Optional.ofNullable(id).orElse(UUID.randomUUID().toString());
    }
}

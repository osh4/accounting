package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;
import java.util.UUID;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDto toDto(Currency model);

    @Mapping(target = "isNewEntity", ignore = true)
    @Mapping(source = "id", target = "id", qualifiedByName = "currencyIdSaveMapper")
    Currency toModel(CurrencyDto dto);

    @Named("currencyIdSaveMapper")
    static String currencyIdSaveMapper(String id) {
        return Optional.ofNullable(id).orElse(UUID.randomUUID().toString());
    }
}

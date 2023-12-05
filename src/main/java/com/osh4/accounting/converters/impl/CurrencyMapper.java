package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Currency;
import org.mapstruct.Mapper;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDto toDto(Currency model);

    Currency toModel(CurrencyDto dto);

}

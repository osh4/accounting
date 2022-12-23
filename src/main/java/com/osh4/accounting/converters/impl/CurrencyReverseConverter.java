package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Currency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
@AllArgsConstructor
public class CurrencyReverseConverter implements Converter<CurrencyDto, Currency> {

    @Override
    public Currency convert(CurrencyDto dto) {
        return isNull(dto) ? null : Currency.builder().id(dto.getId())
                .name(dto.getName())
                .isoCode(dto.getIsoCode())
                .build();
    }
}

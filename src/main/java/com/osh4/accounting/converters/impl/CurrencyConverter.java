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
public class CurrencyConverter implements Converter<Currency, CurrencyDto> {

    @Override
    public CurrencyDto convert(Currency model) {
        return isNull(model) ? null : CurrencyDto.builder()
                .id(model.getId())
                .isoCode(model.getIsoCode())
                .name(model.getName())
                .build();
    }
}

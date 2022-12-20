package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Currency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
@AllArgsConstructor
public class CurrencyReverseConverter implements Converter<CurrencyDto, Mono<Currency>> {

    @Override
    public Mono<Currency> convert(CurrencyDto dto) {
        return Mono.just(Currency.builder().id(dto.getId())
                .name(dto.getName())
                .isoCode(dto.getIsoCode())
                .build());
    }
}

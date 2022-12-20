package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.persistance.r2dbc.TransactionType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
public class TransactionTypeReverseConverter implements Converter<TransactionTypeDto, Mono<TransactionType>> {

    @Override
    public Mono<TransactionType> convert(TransactionTypeDto dto) {
        return Mono.just(TransactionType.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build());
    }
}

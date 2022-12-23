package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.persistance.r2dbc.TransactionType;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
public class TransactionTypeConverter implements Converter<TransactionType, TransactionTypeDto> {
    @Override
    public TransactionTypeDto convert(TransactionType model) {
        return isNull(model) ? null : TransactionTypeDto.builder().id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .build();
    }
}

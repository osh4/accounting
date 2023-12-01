package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
public class TransactionReverseConverter implements Converter<TransactionDto, Transaction> {

    @Override
    public Transaction convert(TransactionDto dto) {
        return isNull(dto) ? null : Transaction.builder()
                .id(createIdIfNeeded(dto.getId()))
                .transactionDate(dto.getDate())
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .transactionTypeId(dto.getType().getId())
                .sourceAccountId(dto.getSource().getId())
                .targetAccountId(dto.getTarget().getId())
                .build();
    }
}

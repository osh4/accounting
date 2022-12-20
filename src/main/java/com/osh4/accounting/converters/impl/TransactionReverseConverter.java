package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
public class TransactionReverseConverter implements Converter<TransactionDto, Mono<Transaction>> {

    @Override
    public Mono<Transaction> convert(TransactionDto dto) {
        return Mono.just(Transaction.builder()
                .id(dto.getId())
                .transactionDate(dto.getDate())
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .transactionTypeId(dto.getType().getId())
                .sourceAccountId(dto.getSource().getId())
                .targetAccountId(dto.getTarget().getId())
                .build());
    }
}

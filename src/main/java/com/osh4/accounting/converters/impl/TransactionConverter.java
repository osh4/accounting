package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import com.osh4.accounting.service.AccountService;
import com.osh4.accounting.service.TransactionTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
@AllArgsConstructor
public class TransactionConverter implements Converter<Transaction, TransactionDto> {
    private TransactionTypeService transactionTypeService;
    private AccountService accountService;

    @Override
    public TransactionDto convert(Transaction model) {
        return TransactionDto.builder()
                .id(model.getId())
                .date(model.getTransactionDate())
                .amount(model.getAmount())
                .description(model.getDescription())
                .type(transactionTypeService.get(model.getTransactionTypeId()).block())
                .source(accountService.get(model.getSourceAccountId()).block())
                .target(accountService.get(model.getTargetAccountId()).block())
                .build();
    }
}

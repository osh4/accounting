package com.osh4.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionDto {
    private String id;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String description;
    private TransactionTypeDto transactionType;
    private AccountDto sourceAccount;
    private AccountDto targetAccount;
}

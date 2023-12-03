package com.osh4.accounting.service.impl;

import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.service.TotalsService;
import com.osh4.accounting.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class TotalsServiceImpl implements TotalsService {
    private final TransactionService transactionService;

    @Override
    public Mono<String> calculateTotals(LocalDate from, LocalDate to) {
        return transactionService.get(from, to)
                .map(list -> list.stream()
                        .map(TransactionDto::getAmount)
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO))
                .map(BigDecimal::toString);
    }
}

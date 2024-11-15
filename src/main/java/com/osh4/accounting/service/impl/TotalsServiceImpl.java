package com.osh4.accounting.service.impl;

import com.osh4.accounting.service.TotalsService;
import com.osh4.accounting.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class TotalsServiceImpl implements TotalsService {
    private final TransactionService transactionService;

    @Override
    public Mono<String> calculateTotalsForPeriod(LocalDate from, LocalDate to) {
        LocalDateTime fromDate = from.atTime(LocalTime.MIDNIGHT);
        LocalDateTime toDate = to.atTime(LocalTime.MAX);
        return transactionService.getAmountForDatePeriod(fromDate, toDate)
                .map(BigDecimal::toString);
    }

    @Override
    public Mono<String> calculateTotalsForDay(LocalDate day) {
        LocalDateTime from = day.atTime(LocalTime.MIDNIGHT);
        LocalDateTime to = day.atTime(LocalTime.MAX);
        return transactionService.getAmountForDatePeriod(from, to)
                .map(BigDecimal::toString);
    }
}

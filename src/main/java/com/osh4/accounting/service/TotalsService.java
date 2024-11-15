package com.osh4.accounting.service;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TotalsService {
    Mono<String> calculateTotalsForPeriod(LocalDate from, LocalDate to);

    Mono<String> calculateTotalsForDay(LocalDate day);

}

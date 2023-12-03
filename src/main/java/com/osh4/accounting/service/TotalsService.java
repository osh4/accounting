package com.osh4.accounting.service;

import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TotalsService {

    Mono<String> calculateTotals(LocalDate from, LocalDate to);

}

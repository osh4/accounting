package com.osh4.accounting.service;

import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TransactionService {

    Mono<Page<TransactionDto>> getAll(PageRequest pageRequest);

    Mono<TransactionDto> get(String id);

    Mono<BigDecimal> getAmountForDatePeriod(LocalDateTime from, LocalDateTime to);

    Mono<TransactionDto> create(TransactionDto dto);

    Mono<TransactionDto> update(String id, TransactionDto dto);

    Mono<Void> delete(String id);

}

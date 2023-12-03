package com.osh4.accounting.service;

import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TransactionService {

    Mono<Page<TransactionDto>> getAll(PageRequest pageRequest);

    Mono<TransactionDto> get(String id);

    Mono<List<TransactionDto>> get(LocalDate from, LocalDate to);

    Mono<Transaction> create(TransactionDto dto);

    Mono<Void> update(String id, TransactionDto dto);

    Mono<Void> delete(String id);

}

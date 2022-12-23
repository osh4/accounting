package com.osh4.accounting.service;

import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TransactionService {

    Flux<TransactionDto> getAll();

    Mono<TransactionDto> get(String id);

    Mono<Transaction> create(TransactionDto dto);

    Mono<Void> update(TransactionDto dto);

    Mono<Void> delete(TransactionDto dto);

}

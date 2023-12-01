package com.osh4.accounting.service;

import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TransactionService {

    Mono<Page<TransactionDto>> getAll(PageRequest pageRequest);

    Mono<TransactionDto> get(String id);

    Mono<Transaction> create(TransactionDto dto);

    Mono<Void> update(String id, TransactionDto dto);

    Mono<Void> delete(String id);

}

package com.osh4.accounting.service;

import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.persistance.r2dbc.TransactionType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TransactionTypeService {
    Flux<TransactionTypeDto> getAll();

    Mono<TransactionTypeDto> get(String id);

    Mono<TransactionType> create(TransactionTypeDto dto);

    Mono<Void> update(TransactionTypeDto dto);

    Mono<Void> delete(TransactionTypeDto dto);
}

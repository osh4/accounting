package com.osh4.accounting.service;

import com.osh4.accounting.dto.TransactionTypeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TransactionTypeService {
    Flux<TransactionTypeDto> getAll();

    Mono<TransactionTypeDto> get(String id);

    Mono<TransactionTypeDto> create(TransactionTypeDto dto);

    Mono<TransactionTypeDto> update(String id, TransactionTypeDto dto);

    Mono<Void> delete(String id);
}

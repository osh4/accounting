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

    Mono<String> create(TransactionTypeDto dto);

    Mono<String> update(TransactionTypeDto dto);

    Mono<String> delete(TransactionTypeDto dto);
}

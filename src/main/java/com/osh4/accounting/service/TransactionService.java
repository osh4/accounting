package com.osh4.accounting.service;

import com.osh4.accounting.dto.TransactionDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TransactionService {

    Flux<TransactionDto> getAll();

    Mono<TransactionDto> get(String id);

    Mono<String> create(TransactionDto dto);

    Mono<String> update(TransactionDto dto);

    Mono<String> delete(TransactionDto dto);

}

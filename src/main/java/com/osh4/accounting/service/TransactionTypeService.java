package com.osh4.accounting.service;

import com.osh4.accounting.dto.TransactionTypeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TransactionTypeService {
    Mono<Page<TransactionTypeDto>> getAll(PageRequest pageRequest);

    Mono<TransactionTypeDto> get(String id);

    Mono<TransactionTypeDto> create(TransactionTypeDto dto);

    Mono<TransactionTypeDto> update(String id, TransactionTypeDto dto);

    Mono<Void> delete(String id);
}

package com.osh4.accounting.service;

import com.osh4.accounting.dto.TransactionCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface TransactionCategoryService {
    Mono<Page<TransactionCategoryDto>> getAll(PageRequest pageRequest);

    Mono<TransactionCategoryDto> get(String id);

    Mono<TransactionCategoryDto> create(TransactionCategoryDto dto);

    Mono<TransactionCategoryDto> update(String id, TransactionCategoryDto dto);

    Mono<Void> delete(String id);
}

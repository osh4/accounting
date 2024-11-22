package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.TransactionCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransactionCategoryRepository extends ReactiveCrudRepository<TransactionCategory, String> {
    Flux<TransactionCategory> findAllBy(Pageable pageable);
}

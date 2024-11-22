package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.TransactionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TransactionTypeRepository extends ReactiveCrudRepository<TransactionType, String> {
    Flux<TransactionType> findAllBy(Pageable pageable);

    Mono<TransactionType> findByName(String name);
}

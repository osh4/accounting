package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction, String> {
    Flux<Transaction> findAllBy(Pageable pageable);

    Flux<Transaction> findAllByTransactionDateBetween(LocalDate from, LocalDate to);
}

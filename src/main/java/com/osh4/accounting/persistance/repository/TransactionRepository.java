package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<Transaction, String> {
    Flux<Transaction> findAllBy(Pageable pageable);

    Flux<Transaction> findAllByTransactionDateBetween(LocalDate from, LocalDate to);

    @Query("SELECT SUM(amount) AS amount FROM Transactions t WHERE t.transaction_date BETWEEN :from AND :to")
    Mono<BigDecimal> findByTransactionDateBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}

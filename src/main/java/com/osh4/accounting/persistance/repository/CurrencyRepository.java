package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.Currency;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CurrencyRepository extends ReactiveCrudRepository<Currency, String> {
    Flux<Currency> findAllBy(Pageable pageable);
}

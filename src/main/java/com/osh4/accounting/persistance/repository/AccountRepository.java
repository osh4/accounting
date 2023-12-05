package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, String> {
    Flux<Account> findAllBy(Pageable pageable);
}

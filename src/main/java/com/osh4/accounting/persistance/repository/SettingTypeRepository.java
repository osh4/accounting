package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.SettingType;
import com.osh4.accounting.persistance.r2dbc.TransactionCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SettingTypeRepository extends ReactiveCrudRepository<SettingType, String> {
    Flux<SettingType> findAllBy(Pageable pageable);

    Mono<SettingType> findByName(String name);
}

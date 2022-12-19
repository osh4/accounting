package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.Setting;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SettingsRepository extends ReactiveCrudRepository<Setting, Long> {

    Mono<Setting> findByKey(String key);
}

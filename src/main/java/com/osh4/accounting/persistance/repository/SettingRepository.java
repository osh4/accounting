package com.osh4.accounting.persistance.repository;

import com.osh4.accounting.persistance.r2dbc.Setting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SettingRepository extends ReactiveCrudRepository<Setting, String> {
    Flux<Setting> findAllBy(Pageable pageable);
}

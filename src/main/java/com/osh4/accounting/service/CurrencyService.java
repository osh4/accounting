package com.osh4.accounting.service;

import com.osh4.accounting.dto.CurrencyDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {

    Flux<CurrencyDto> getAll();
    Mono<CurrencyDto> get(String id);
    Mono<String> create(CurrencyDto dto);

    Mono<String> update(CurrencyDto dto);

    Mono<String> delete(CurrencyDto dto);

}

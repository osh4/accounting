package com.osh4.accounting.service;

import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Currency;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {

    Flux<CurrencyDto> getAll();

    Mono<CurrencyDto> get(String id);

    Mono<Currency> create(CurrencyDto dto);

    Mono<Void> update(CurrencyDto dto);

    Mono<Void> delete(CurrencyDto dto);

}

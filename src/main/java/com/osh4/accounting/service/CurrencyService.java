package com.osh4.accounting.service;

import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

public interface CurrencyService {

    Mono<Page<CurrencyDto>> getAll(PageRequest pageRequest);

    Mono<CurrencyDto> get(String id);

    Mono<Currency> create(CurrencyDto dto);

    Mono<Void> update(CurrencyDto dto);

    Mono<Void> delete(CurrencyDto dto);

}

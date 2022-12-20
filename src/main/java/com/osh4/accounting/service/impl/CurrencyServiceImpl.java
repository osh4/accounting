package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Currency;
import com.osh4.accounting.persistance.repository.CurrencyRepository;
import com.osh4.accounting.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private CurrencyRepository currencyRepository;
    private Converter<Currency, CurrencyDto> currencyConverter;
    private Converter<CurrencyDto, Mono<Currency>> currencyReverseConverter;

    @Override
    public Flux<CurrencyDto> getAll() {
        return currencyRepository.findAll()
                .map(currencyConverter::convert);
    }

    @Override
    public Mono<CurrencyDto> get(String id) {
        return currencyRepository.findById(id)
                .map(currencyConverter::convert);
    }


    @Override
    public Mono<String> create(CurrencyDto dto) {
        return currencyRepository.findById(dto.getId())
                .flatMap(x -> Mono.error(new DuplicateKeyException("AlreadyExist")))
                .switchIfEmpty(currencyReverseConverter.convert(dto).flatMap(currencyRepository::save))
                .flatMap(x -> Mono.just("Add Success"));
    }

    @Override
    public Mono<String> update(CurrencyDto dto) {
        return currencyRepository.findById(dto.getId())
                .map(currency -> setValues(currency, dto))
                .flatMap(x -> Mono.just("Successful update!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with update")));
    }

    @Override
    public Mono<String> delete(CurrencyDto dto) {
        return currencyRepository.findById(dto.getId())
                .map(currencyRepository::delete)
                .flatMap(x -> Mono.just("Successful remove!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with removal")));
    }

    private Currency setValues(Currency currency, CurrencyDto dto) {
        if (nonNull(dto.getIsoCode()) && ObjectUtils.notEqual(dto.getIsoCode(), currency.getIsoCode())) {
            currency.setIsoCode(dto.getIsoCode());
        }
        if (nonNull(dto.getName()) && ObjectUtils.notEqual(dto.getName(), currency.getName())) {
            currency.setName(dto.getName());
        }
        return currency;
    }
}

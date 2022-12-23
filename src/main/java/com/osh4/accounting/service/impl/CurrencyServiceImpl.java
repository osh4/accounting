package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Currency;
import com.osh4.accounting.persistance.repository.CurrencyRepository;
import com.osh4.accounting.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private CurrencyRepository currencyRepository;
    private Converter<Currency, CurrencyDto> currencyConverter;
    private Converter<CurrencyDto, Currency> currencyReverseConverter;

    @Override
    public Flux<CurrencyDto> getAll() {
        return currencyRepository.findAll()
                .map(currencyConverter::convert);
    }

    @Override
    public Mono<CurrencyDto> get(String id) {
        return currencyRepository.findById(id)
                .map(currencyConverter::convert)
                .switchIfEmpty(Mono.error(new Exception()));
    }


    @Override
    public Mono<Currency> create(CurrencyDto dto) {
        return currencyRepository.save(currencyReverseConverter.convert(dto).setAsNew());
    }

    @Override
    public Mono<Void> update(CurrencyDto dto) {
        return currencyRepository.findById(dto.getId())
                .flatMap(model -> updateFields(model, dto))
                .then();
    }

    @Override
    public Mono<Void> delete(CurrencyDto dto) {
        return currencyRepository.deleteById(dto.getId());
    }

    private Mono<Currency> updateFields(Currency model, CurrencyDto dto) {
        if (nonNull(dto.getIsoCode()) && ObjectUtils.notEqual(dto.getIsoCode(), model.getIsoCode())) {
            model.setIsoCode(dto.getIsoCode());
        }
        if (nonNull(dto.getName()) && ObjectUtils.notEqual(dto.getName(), model.getName())) {
            model.setName(dto.getName());
        }
        return currencyRepository.save(model);
    }
}

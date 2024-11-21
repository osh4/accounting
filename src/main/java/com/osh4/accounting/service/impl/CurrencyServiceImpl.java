package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.CurrencyMapper;
import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.exception.AlreadyExistsException;
import com.osh4.accounting.exception.NotFoundException;
import com.osh4.accounting.persistance.r2dbc.Currency;
import com.osh4.accounting.persistance.repository.CurrencyRepository;
import com.osh4.accounting.service.CurrencyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Slf4j
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private CurrencyRepository currencyRepository;
    private CurrencyMapper currencyMapper;

    @Override
    public Mono<Page<CurrencyDto>> getAll(PageRequest pageRequest) {
        return currencyRepository.findAllBy(pageRequest)
                .map(currencyMapper::toDto)
                .collectList()
                .zipWith(currencyRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    @Override
    public Mono<CurrencyDto> get(String id) {
        return currencyRepository.findById(id)
                .map(currencyMapper::toDto)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<CurrencyDto> getByIsocode(String isocode) {
        return currencyRepository.findByIsoCode(isocode)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorResume(it -> Mono.empty())
                .map(currencyMapper::toDto)
                .switchIfEmpty(Mono.error(NotFoundException.fromCurrencyIsoCode(isocode)));
    }

    @Override
    public Mono<CurrencyDto> create(CurrencyDto dto) {
        return currencyRepository.findByIsoCode(dto.getIsoCode())
                .switchIfEmpty(Mono.just(currencyMapper.toModel(dto).setAsNew()).flatMap(currencyRepository::save))
                .filter(Currency::isNewEntity)
                .map(currencyMapper::toDto)
                .switchIfEmpty(Mono.error(AlreadyExistsException.fromCurrencyIsoCode(dto.getIsoCode())));
    }

    @Override
    public Mono<CurrencyDto> update(String isoCode, CurrencyDto dto) {
        return currencyRepository.findByIsoCode(isoCode)
                .switchIfEmpty(Mono.error(NotFoundException.fromCurrencyIsoCode(isoCode)))
                .flatMap(model -> updateFields(model, dto))
                .map(currencyMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String isoCode) {
        return currencyRepository.findByIsoCode(isoCode)
                .switchIfEmpty(Mono.error(NotFoundException.fromCurrencyIsoCode(isoCode)))
                .flatMap(currency -> currencyRepository.deleteById(currency.getId()));
    }

    private Mono<Currency> updateFields(Currency model, CurrencyDto dto) {
        if (isNull(dto)) {
            return Mono.just(model);
        }
        if (isNotBlank(dto.getIsoCode()) && ObjectUtils.notEqual(dto.getIsoCode(), model.getIsoCode())) {
            model.setIsoCode(dto.getIsoCode());
        }
        if (isNotBlank(dto.getName()) && ObjectUtils.notEqual(dto.getName(), model.getName())) {
            model.setName(dto.getName());
        }
        if (isNotBlank(dto.getLongName()) && ObjectUtils.notEqual(dto.getLongName(), model.getLongName())) {
            model.setLongName(dto.getLongName());
        }
        return currencyRepository.save(model);
    }
}

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
    private CurrencyRepository repository;
    private CurrencyMapper mapper;

    @Override
    public Mono<Page<CurrencyDto>> getAll(PageRequest pageRequest) {
        return repository.findAllBy(pageRequest)
                .map(mapper::toDto)
                .collectList()
                .zipWith(repository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    @Override
    public Mono<CurrencyDto> get(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<CurrencyDto> getByIsocode(String isocode) {
        return repository.findByIsoCode(isocode)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorResume(it -> Mono.empty())
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(NotFoundException.fromCurrencyIsoCode(isocode)));
    }

    @Override
    public Mono<CurrencyDto> create(CurrencyDto dto) {
        return repository.findByIsoCode(dto.getIsoCode())
                .switchIfEmpty(Mono.just(mapper.toModel(dto).setAsNew()).flatMap(repository::save))
                .filter(Currency::isNewEntity)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(AlreadyExistsException.fromCurrencyIsoCode(dto.getIsoCode())));
    }

    @Override
    public Mono<CurrencyDto> update(String isoCode, CurrencyDto dto) {
        return repository.findByIsoCode(isoCode)
                .switchIfEmpty(Mono.error(NotFoundException.fromCurrencyIsoCode(isoCode)))
                .flatMap(model -> updateFields(model, dto))
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(String isoCode) {
        return repository.findByIsoCode(isoCode)
                .switchIfEmpty(Mono.error(NotFoundException.fromCurrencyIsoCode(isoCode)))
                .flatMap(currency -> repository.deleteById(currency.getId()));
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
        return repository.save(model);
    }
}

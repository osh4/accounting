package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.CurrencyMapper;
import com.osh4.accounting.dto.CurrencyDto;
import com.osh4.accounting.persistance.r2dbc.Currency;
import com.osh4.accounting.persistance.repository.CurrencyRepository;
import com.osh4.accounting.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
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
    public Mono<Currency> create(CurrencyDto dto) {
        return currencyRepository.save(currencyMapper.toModel(dto).setAsNew());
    }

    @Override
    public Mono<Void> update(String id, CurrencyDto dto) {
        return currencyRepository.findById(id)
                .flatMap(model -> updateFields(model, dto))
                .then();
    }

    @Override
    public Mono<Void> delete(String id) {
        return currencyRepository.deleteById(id);
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
        return currencyRepository.save(model);
    }
}

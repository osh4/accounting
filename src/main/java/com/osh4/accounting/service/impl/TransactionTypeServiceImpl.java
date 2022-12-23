package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.persistance.r2dbc.TransactionType;
import com.osh4.accounting.persistance.repository.TransactionTypeRepository;
import com.osh4.accounting.service.TransactionTypeService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.nonNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
@AllArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {
    private TransactionTypeRepository transactionTypeRepository;
    private Converter<TransactionType, TransactionTypeDto> transactionTypeConverter;
    private Converter<TransactionTypeDto, TransactionType> transactionTypeReverseConverter;


    @Override
    public Flux<TransactionTypeDto> getAll() {
        return transactionTypeRepository.findAll().map(transactionTypeConverter::convert);
    }


    @Override
    public Mono<TransactionTypeDto> get(String id) {
        return transactionTypeRepository.findById(id)
                .map(transactionTypeConverter::convert)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<TransactionType> create(TransactionTypeDto dto) {
        return transactionTypeRepository.save(transactionTypeReverseConverter.convert(dto).setAsNew());
    }


    @Override
    public Mono<Void> update(TransactionTypeDto dto) {
        return transactionTypeRepository.findById(dto.getId())
                .flatMap(model -> updateFields(model, dto))
                .then();
    }

    @Override
    public Mono<Void> delete(TransactionTypeDto dto) {
        return transactionTypeRepository.deleteById(dto.getId());
    }

    private Mono<TransactionType> updateFields(TransactionType model, TransactionTypeDto dto) {
        if (nonNull(dto.getName()) && ObjectUtils.notEqual(dto.getName(), model.getName())) {
            model.setName(dto.getName());
        }
        if (nonNull(dto.getDescription()) && ObjectUtils.notEqual(dto.getDescription(), model.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        return transactionTypeRepository.save(model);
    }

}

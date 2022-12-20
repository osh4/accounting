package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.persistance.r2dbc.TransactionType;
import com.osh4.accounting.persistance.repository.TransactionTypeRepository;
import com.osh4.accounting.service.TransactionTypeService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.DuplicateKeyException;
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
    private Converter<TransactionTypeDto, Mono<TransactionType>> transactionTypeReverseConverter;


    @Override
    public Flux<TransactionTypeDto> getAll() {
        return transactionTypeRepository.findAll().map(transactionTypeConverter::convert);
    }


    @Override
    public Mono<TransactionTypeDto> get(String id) {
        return transactionTypeRepository.findById(id).map(transactionTypeConverter::convert);
    }

    @Override
    public Mono<String> create(TransactionTypeDto dto) {
        return transactionTypeRepository.findById(dto.getId())
                .flatMap(x -> Mono.error(new DuplicateKeyException("AlreadyExist")))
                .switchIfEmpty(transactionTypeReverseConverter.convert(dto).flatMap(transactionTypeRepository::save))
                .flatMap(x -> Mono.just("Add Success"));
    }


    @Override
    public Mono<String> update(TransactionTypeDto dto) {
        return transactionTypeRepository.findById(dto.getId())
                .map(currency -> setValues(currency, dto))
                .flatMap(x -> Mono.just("Successful update!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with update")));
    }

    @Override
    public Mono<String> delete(TransactionTypeDto dto) {
        return transactionTypeRepository.findById(dto.getId())
                .map(transactionTypeRepository::delete)
                .flatMap(x -> Mono.just("Successful remove!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with removal")));
    }

    private TransactionType setValues(TransactionType model, TransactionTypeDto dto) {
        if (nonNull(dto.getName()) && ObjectUtils.notEqual(dto.getName(), model.getName())) {
            model.setName(dto.getName());
        }
        if (nonNull(dto.getDescription()) && ObjectUtils.notEqual(dto.getDescription(), model.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        return model;
    }

}

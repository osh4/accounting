package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.TransactionTypeMapper;
import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.persistance.r2dbc.TransactionType;
import com.osh4.accounting.persistance.repository.TransactionTypeRepository;
import com.osh4.accounting.service.TransactionTypeService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
@AllArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {
    private TransactionTypeRepository transactionTypeRepository;
    private TransactionTypeMapper transactionTypeMapper;

    @Override
    public Flux<TransactionTypeDto> getAll() {
        return transactionTypeRepository.findAll().map(transactionTypeMapper::toDto);
    }


    @Override
    public Mono<TransactionTypeDto> get(String id) {
        return transactionTypeRepository.findById(id)
                .map(transactionTypeMapper::toDto)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<TransactionType> create(TransactionTypeDto dto) {
        return transactionTypeRepository.save(transactionTypeMapper.toModel(dto).setAsNew());
    }


    @Override
    public Mono<Void> update(String id, TransactionTypeDto dto) {
        return transactionTypeRepository.findById(id)
                .flatMap(model -> updateFields(model, dto))
                .then();
    }

    @Override
    public Mono<Void> delete(String id) {
        return transactionTypeRepository.deleteById(id);
    }

    private Mono<TransactionType> updateFields(TransactionType model, TransactionTypeDto dto) {
        if (isNull(dto)) {
            return Mono.just(model);
        }
        if (isNotBlank(dto.getName()) && ObjectUtils.notEqual(dto.getName(), model.getName())) {
            model.setName(dto.getName());
        }
        if (isNotBlank(dto.getDescription()) && ObjectUtils.notEqual(dto.getDescription(), model.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        return transactionTypeRepository.save(model);
    }

}

package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.TransactionTypeMapper;
import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.exception.AlreadyExistsException;
import com.osh4.accounting.exception.NotFoundException;
import com.osh4.accounting.persistance.r2dbc.TransactionType;
import com.osh4.accounting.persistance.repository.TransactionTypeRepository;
import com.osh4.accounting.service.TransactionTypeService;
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

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
@Slf4j
@AllArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {
    private TransactionTypeRepository repository;
    private TransactionTypeMapper mapper;

    @Override
    public Mono<Page<TransactionTypeDto>> getAll(PageRequest pageRequest) {
        return repository.findAllBy(pageRequest)
                .map(mapper::toDto)
                .collectList()
                .zipWith(repository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    @Override
    public Mono<TransactionTypeDto> get(String id) {
        return repository.findById(id)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorResume(it -> Mono.empty())
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(NotFoundException.fromTransactionTypeId(id)));
    }

    @Override
    public Mono<TransactionTypeDto> create(TransactionTypeDto dto) {
        return repository.findByName(dto.getName())
                .switchIfEmpty(Mono.just(mapper.toModel(dto).setAsNew()).flatMap(repository::save))
                .filter(TransactionType::isNewEntity)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(AlreadyExistsException.fromTransactionTypeName(dto.getName())));
    }

    @Override
    public Mono<TransactionTypeDto> update(String id, TransactionTypeDto dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.fromTransactionTypeId(id)))
                .flatMap(model -> updateFields(model, dto))
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.fromTransactionTypeId(id)))
                .flatMap(account -> repository.deleteById(id));
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
        return repository.save(model);
    }

}

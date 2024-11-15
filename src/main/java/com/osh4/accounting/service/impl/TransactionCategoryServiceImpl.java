package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.TransactionCategoryMapper;
import com.osh4.accounting.dto.TransactionCategoryDto;
import com.osh4.accounting.persistance.r2dbc.TransactionCategory;
import com.osh4.accounting.persistance.repository.TransactionCategoryRepository;
import com.osh4.accounting.service.TransactionCategoryService;
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
public class TransactionCategoryServiceImpl implements TransactionCategoryService {
    private TransactionCategoryRepository repository;
    private TransactionCategoryMapper mapper;

    @Override
    public Flux<TransactionCategoryDto> getAll() {
        return repository.findAll().map(mapper::toDto);
    }

    @Override
    public Mono<TransactionCategoryDto> get(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<TransactionCategoryDto> create(TransactionCategoryDto dto) {
        return repository.save(mapper.toModel(dto).setAsNew()).map(mapper::toDto);
    }

    @Override
    public Mono<TransactionCategoryDto> update(String id, TransactionCategoryDto dto) {
        return repository.findById(id)
                .flatMap(model -> updateFields(model, dto))
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    private Mono<TransactionCategory> updateFields(TransactionCategory model, TransactionCategoryDto dto) {
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

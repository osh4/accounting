package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.TransactionCategoryMapper;
import com.osh4.accounting.dto.TransactionCategoryDto;
import com.osh4.accounting.exception.AlreadyExistsException;
import com.osh4.accounting.exception.NotFoundException;
import com.osh4.accounting.persistance.r2dbc.TransactionCategory;
import com.osh4.accounting.persistance.repository.TransactionCategoryRepository;
import com.osh4.accounting.service.TransactionCategoryService;
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
public class TransactionCategoryServiceImpl implements TransactionCategoryService {
    private TransactionCategoryRepository repository;
    private TransactionCategoryMapper mapper;

    @Override
    public Mono<Page<TransactionCategoryDto>> getAll(PageRequest pageRequest) {
        return repository.findAllBy(pageRequest)
                .map(mapper::toDto)
                .collectList()
                .zipWith(repository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    @Override
    public Mono<TransactionCategoryDto> get(String id) {
        return repository.findById(id)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorResume(it -> Mono.empty())
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(NotFoundException.fromTransactionCategoryId(id)));
    }

    @Override
    public Mono<TransactionCategoryDto> create(TransactionCategoryDto dto) {
        return repository.findById(dto.getId())
                .switchIfEmpty(Mono.just(mapper.toModel(dto).setAsNew()).flatMap(repository::save))
                .filter(TransactionCategory::isNewEntity)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(AlreadyExistsException.fromTransactionCategoryId(dto.getId())));
    }

    @Override
    public Mono<TransactionCategoryDto> update(String id, TransactionCategoryDto dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.fromTransactionCategoryId(id)))
                .flatMap(model -> updateFields(model, dto))
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.fromTransactionCategoryId(id)))
                .flatMap(account -> repository.deleteById(id));
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

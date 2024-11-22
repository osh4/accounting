package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.TransactionMapper;
import com.osh4.accounting.dto.AccountDto;
import com.osh4.accounting.dto.TransactionCategoryDto;
import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.exception.AlreadyExistsException;
import com.osh4.accounting.exception.NotFoundException;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import com.osh4.accounting.persistance.repository.TransactionRepository;
import com.osh4.accounting.service.AccountService;
import com.osh4.accounting.service.TransactionCategoryService;
import com.osh4.accounting.service.TransactionService;
import com.osh4.accounting.service.TransactionTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
@Slf4j
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository repository;
    private TransactionTypeService transactionTypeService;
    private TransactionCategoryService transactionCategoryService;
    private AccountService accountService;
    private TransactionMapper mapper;

    @Override
    public Mono<Page<TransactionDto>> getAll(PageRequest pageRequest) {
        return repository.findAllBy(pageRequest)
                .map(mapper::toDto)
                .flatMap(this::populateSourceAccount)
                .flatMap(this::populateTargetAccount)
                .flatMap(this::populateTransactionType)
                .flatMap(this::populateTransactionCategory)
                .collectList()
                .zipWith(repository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    private Mono<TransactionDto> populateTransactionCategory(TransactionDto dto) {
        return Mono.justOrEmpty(dto.getTransactionCategory())
                .filter(Objects::nonNull)
                .map(TransactionCategoryDto::getId)
                .filter(StringUtils::isNotBlank)
                .flatMap(transactionCategoryService::get)
                .map(settingType -> {
                    dto.setTransactionCategory(settingType);
                    return dto;
                })
                .switchIfEmpty(Mono.just(dto))
                .onErrorReturn(dto);
    }

    private Mono<TransactionDto> populateTransactionType(TransactionDto dto) {
        return Mono.justOrEmpty(dto.getTransactionType())
                .filter(Objects::nonNull)
                .map(TransactionTypeDto::getId)
                .filter(StringUtils::isNotBlank)
                .flatMap(transactionTypeService::get)
                .map(transactionType -> {
                    dto.setTransactionType(transactionType);
                    return dto;
                })
                .switchIfEmpty(Mono.just(dto))
                .onErrorReturn(dto);
    }

    private Mono<TransactionDto> populateSourceAccount(TransactionDto dto) {
        return Mono.justOrEmpty(dto.getSourceAccount())
                .filter(Objects::nonNull)
                .map(AccountDto::getId)
                .filter(StringUtils::isNotBlank)
                .flatMap(accountService::get)
                .map(account -> {
                    dto.setSourceAccount(account);
                    return dto;
                })
                .switchIfEmpty(Mono.just(dto))
                .onErrorReturn(dto);
    }

    private Mono<TransactionDto> populateTargetAccount(TransactionDto dto) {
        return Mono.justOrEmpty(dto.getTargetAccount())
                .filter(Objects::nonNull)
                .map(AccountDto::getId)
                .filter(StringUtils::isNotBlank)
                .flatMap(accountService::get)
                .map(account -> {
                    dto.setTargetAccount(account);
                    return dto;
                })
                .switchIfEmpty(Mono.just(dto))
                .onErrorReturn(dto);
    }

    @Override
    public Mono<TransactionDto> get(String id) {
        return repository.findById(id)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorResume(it -> Mono.empty())
                .map(mapper::toDto)
                .flatMap(this::populateSourceAccount)
                .flatMap(this::populateTargetAccount)
                .flatMap(this::populateTransactionType)
                .flatMap(this::populateTransactionCategory)
                .switchIfEmpty(Mono.error(NotFoundException.fromTransactionId(id)));
    }

    @Override
    public Mono<BigDecimal> getAmountForDatePeriod(LocalDateTime from, LocalDateTime to) {
        return repository.findByTransactionDateBetween(from, to);
    }

    @Override
    public Mono<TransactionDto> create(TransactionDto dto) {
        return repository.findById(dto.getId())
                .switchIfEmpty(Mono.just(mapper.toModel(dto).setAsNew()).flatMap(repository::save))
                .filter(Transaction::isNewEntity)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(AlreadyExistsException.fromTransactionId(dto.getId())));
    }

    @Override
    public Mono<TransactionDto> update(String id, TransactionDto dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.fromTransactionId(id)))
                .flatMap(model -> updateFields(model, dto))
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.fromTransactionId(id)))
                .flatMap(account -> repository.deleteById(id));
    }

    private Mono<Transaction> updateFields(Transaction model, TransactionDto dto) {
        if (isNull(dto)) {
            return Mono.just(model);
        }
        if (nonNull(dto.getTransactionDate()) && ObjectUtils.notEqual(dto.getTransactionDate(), model.getTransactionDate())) {
            model.setTransactionDate(dto.getTransactionDate());
        }
        if (nonNull(dto.getAmount()) && ObjectUtils.notEqual(dto.getAmount(), model.getAmount())) {
            model.setAmount(dto.getAmount());
        }
        if (isNotBlank(dto.getDescription()) && ObjectUtils.notEqual(dto.getDescription(), model.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        if (nonNull(dto.getTransactionType()) && isNotBlank(dto.getTransactionType().getId()) && ObjectUtils.notEqual(dto.getTransactionType().getId(), model.getTransactionTypeId())) {
            model.setTransactionTypeId(dto.getTransactionType().getId());
        }
        if (nonNull(dto.getSourceAccount()) && isNotBlank(dto.getSourceAccount().getId()) && ObjectUtils.notEqual(dto.getSourceAccount().getId(), model.getSourceAccountId())) {
            model.setSourceAccountId(dto.getSourceAccount().getId());
        }
        if (nonNull(dto.getTargetAccount()) && isNotBlank(dto.getTargetAccount().getId()) && ObjectUtils.notEqual(dto.getTargetAccount().getId(), model.getTargetAccountId())) {
            model.setTargetAccountId(dto.getTargetAccount().getId());
        }

        return repository.save(model);
    }

}

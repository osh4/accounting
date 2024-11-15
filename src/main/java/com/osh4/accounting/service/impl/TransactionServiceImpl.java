package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.TransactionMapper;
import com.osh4.accounting.dto.TransactionCategoryDto;
import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import com.osh4.accounting.persistance.repository.AccountRepository;
import com.osh4.accounting.persistance.repository.TransactionRepository;
import com.osh4.accounting.persistance.repository.TransactionTypeRepository;
import com.osh4.accounting.service.TransactionCategoryService;
import com.osh4.accounting.service.TransactionService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private TransactionTypeRepository transactionTypeRepository;
    private TransactionCategoryService transactionCategoryService;
    private AccountRepository accountRepository;
    private TransactionMapper transactionMapper;

    @Override
    public Mono<Page<TransactionDto>> getAll(PageRequest pageRequest) {
        return transactionRepository.findAllBy(pageRequest)
                .flatMap(this::populateTransactionType)
                .flatMap(this::populateSourceAccount)
                .flatMap(this::populateTargetAccount)
                .map(transactionMapper::toDto)
                .flatMap(this::populateTransactionCategory)
                .collectList()
                .zipWith(transactionRepository.count())
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

    private Mono<Transaction> populateTransactionType(Transaction transaction) {
        return transactionTypeRepository.findById(transaction.getTransactionTypeId())
                .map(transactionType -> {
                    transaction.setTransactionType(transactionType);
                    return transaction;
                });
    }

    private Mono<Transaction> populateSourceAccount(Transaction transaction) {
        return accountRepository.findById(transaction.getSourceAccountId())
                .map(account -> {
                    transaction.setSourceAccount(account);
                    return transaction;
                });
    }

    private Mono<Transaction> populateTargetAccount(Transaction transaction) {
        return accountRepository.findById(transaction.getTargetAccountId())
                .map(account -> {
                    transaction.setTargetAccount(account);
                    return transaction;
                });
    }

    @Override
    public Mono<TransactionDto> get(String id) {
        return transactionRepository.findById(id)
                .flatMap(this::populateTransactionType)
                .flatMap(this::populateSourceAccount)
                .flatMap(this::populateTargetAccount)
                .map(transactionMapper::toDto)
                .flatMap(this::populateTransactionCategory)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<List<TransactionDto>> get(LocalDate from, LocalDate to) {
        return transactionRepository.findAllByTransactionDateBetween(from, to)
                .flatMap(this::populateTransactionType)
                .flatMap(this::populateSourceAccount)
                .flatMap(this::populateTargetAccount)
                .map(transactionMapper::toDto)
                .collectList();
    }

    @Override
    public Mono<Transaction> create(TransactionDto dto) {
        return transactionRepository.save(transactionMapper.toModel(dto).setAsNew());
    }

    @Override
    public Mono<TransactionDto> create(TransactionDto dto) {
        return transactionRepository.save(transactionMapper.toModel(dto).setAsNew()).map(transactionMapper::toDto);
    }

    @Override
    public Mono<TransactionDto> update(String id, TransactionDto dto) {
        return transactionRepository.findById(id)
                .flatMap(model -> updateFields(model, dto))
                .map(transactionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return transactionRepository.deleteById(id);
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

        return transactionRepository.save(model);
    }

}

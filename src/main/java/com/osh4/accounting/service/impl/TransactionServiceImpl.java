package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import com.osh4.accounting.persistance.repository.TransactionRepository;
import com.osh4.accounting.service.TransactionService;
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
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private Converter<Transaction, TransactionDto> transactionConverter;
    private Converter<TransactionDto, Mono<Transaction>> transactionReverseConverter;

    @Override
    public Flux<TransactionDto> getAll() {
        return transactionRepository.findAll()
                .map(transactionConverter::convert);
    }

    @Override
    public Mono<TransactionDto> get(String id) {
        return transactionRepository.findById(id)
                .map(transactionConverter::convert);
    }

    @Override
    public Mono<String> create(TransactionDto dto) {
        return transactionRepository.findById(dto.getId())
                .flatMap(x -> Mono.error(new DuplicateKeyException("AlreadyExist")))
                .switchIfEmpty(transactionReverseConverter.convert(dto).flatMap(transactionRepository::save))
                .flatMap(x -> Mono.just("Add Success"));
    }

    @Override
    public Mono<String> update(TransactionDto dto) {
        return transactionRepository.findById(dto.getId())
                .map(currency -> setValues(currency, dto))
                .flatMap(x -> Mono.just("Successful update!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with update")));
    }

    @Override
    public Mono<String> delete(TransactionDto dto) {
        return transactionRepository.findById(dto.getId())
                .map(transactionRepository::delete)
                .flatMap(x -> Mono.just("Successful remove!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with removal")));
    }


    private Transaction setValues(Transaction model, TransactionDto dto) {
        if (nonNull(dto.getDate()) && ObjectUtils.notEqual(dto.getDate(), model.getTransactionDate())) {
            model.setTransactionDate(dto.getDate());
        }
        if (nonNull(dto.getAmount()) && ObjectUtils.notEqual(dto.getAmount(), model.getAmount())) {
            model.setAmount(dto.getAmount());
        }
        if (nonNull(dto.getDescription()) && ObjectUtils.notEqual(dto.getDescription(), model.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        if (nonNull(dto.getType()) && ObjectUtils.notEqual(dto.getType().getId(), model.getTransactionTypeId())) {
            model.setTransactionTypeId(dto.getType().getId());
        }
        if (nonNull(dto.getSource()) && ObjectUtils.notEqual(dto.getSource().getId(), model.getSourceAccountId())) {
            model.setSourceAccountId(dto.getSource().getId());
        }
        if (nonNull(dto.getTarget()) && ObjectUtils.notEqual(dto.getTarget().getId(), model.getTargetAccountId())) {
            model.setTargetAccountId(dto.getTarget().getId());
        }

        return model;
    }

}

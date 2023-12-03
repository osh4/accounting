package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.TransactionMapper;
import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import com.osh4.accounting.persistance.repository.AccountRepository;
import com.osh4.accounting.persistance.repository.TransactionRepository;
import com.osh4.accounting.persistance.repository.TransactionTypeRepository;
import com.osh4.accounting.service.TransactionService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    private AccountRepository accountRepository;
    private TransactionMapper transactionMapper;
//    private AccountService accountService;

    @Override
    public Mono<Page<TransactionDto>> getAll(PageRequest pageRequest) {
        return transactionRepository.findAllBy(pageRequest)
                .flatMap(this::populateTransactionType)
                .flatMap(this::populateSourceAccount)
                .flatMap(this::populateTargetAccount)
                .map(transactionMapper::toDto)
                .collectList()
                .zipWith(transactionRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    private Publisher<? extends Transaction> populateTransactionType(Transaction transaction) {
        return transactionTypeRepository.findById(transaction.getTransactionTypeId())
                .map(transactionType -> {
                    transaction.setTransactionType(transactionType);
                    return transaction;
                });
    }

    private Publisher<? extends Transaction> populateSourceAccount(Transaction transaction) {
        return accountRepository.findById(transaction.getSourceAccountId())
                .map(account -> {
                    transaction.setSourceAccount(account);
                    return transaction;
                });
    }

    private Publisher<? extends Transaction> populateTargetAccount(Transaction transaction) {
        return accountRepository.findById(transaction.getTargetAccountId())
                .map(account -> {
                    transaction.setTargetAccount(account);
                    return transaction;
                });
    }

    @Override
    public Mono<TransactionDto> get(String id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toDto)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<Transaction> create(TransactionDto dto) {
//        String sourceAccountId = dto.getSourceAccount().getId();
//        AccountDto sourceAccountDto = accountService.get(sourceAccountId).block();
//        dto.setSourceAccount(sourceAccountDto);
//        AccountDto targetAccountDto = accountService.get(dto.getTargetAccount().getId()).block();
//        dto.setTargetAccount(targetAccountDto);
//
        Transaction model = transactionMapper.toModel(dto).setAsNew();

        return transactionRepository.save(model);
    }

    @Override
    public Mono<Void> update(String id, TransactionDto dto) {
        return transactionRepository.findById(id)
                .flatMap(model -> updateFields(model, dto))
                .then();
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

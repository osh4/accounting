package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.persistance.r2dbc.Transaction;
import com.osh4.accounting.persistance.repository.TransactionRepository;
import com.osh4.accounting.service.TransactionService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
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
    private Converter<Transaction, TransactionDto> transactionConverter;
    private Converter<TransactionDto, Transaction> transactionReverseConverter;

    @Override
    public Mono<Page<TransactionDto>> getAll(PageRequest pageRequest) {
        return transactionRepository.findAllBy(pageRequest)
                .map(transactionConverter::convert)
                .collectList()
                .zipWith(transactionRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    @Override
    public Mono<TransactionDto> get(String id) {
        return transactionRepository.findById(id)
                .map(transactionConverter::convert)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<Transaction> create(TransactionDto dto) {
        return transactionRepository.save(transactionReverseConverter.convert(dto).setAsNew());
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
        if (nonNull(dto.getDate()) && ObjectUtils.notEqual(dto.getDate(), model.getTransactionDate())) {
            model.setTransactionDate(dto.getDate());
        }
        if (nonNull(dto.getAmount()) && ObjectUtils.notEqual(dto.getAmount(), model.getAmount())) {
            model.setAmount(dto.getAmount());
        }
        if (isNotBlank(dto.getDescription()) && ObjectUtils.notEqual(dto.getDescription(), model.getDescription())) {
            model.setDescription(dto.getDescription());
        }
        if (nonNull(dto.getType()) && isNotBlank(dto.getType().getId()) && ObjectUtils.notEqual(dto.getType().getId(), model.getTransactionTypeId())) {
            model.setTransactionTypeId(dto.getType().getId());
        }
        if (nonNull(dto.getSource()) && isNotBlank(dto.getSource().getId()) && ObjectUtils.notEqual(dto.getSource().getId(), model.getSourceAccountId())) {
            model.setSourceAccountId(dto.getSource().getId());
        }
        if (nonNull(dto.getTarget()) && isNotBlank(dto.getTarget().getId()) && ObjectUtils.notEqual(dto.getTarget().getId(), model.getTargetAccountId())) {
            model.setTargetAccountId(dto.getTarget().getId());
        }

        return transactionRepository.save(model);
    }

}

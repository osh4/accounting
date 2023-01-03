package com.osh4.accounting.controller;

import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transaction")
@Slf4j
@AllArgsConstructor
public class TransactionController extends BaseController {

    private final TransactionService transactionService;

    @GetMapping
    public Mono<Page<TransactionDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id_asc") String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, createSort(sort));
        return transactionService.getAll(pageRequest);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TransactionDto>> get(@PathVariable String id) {
        return transactionService.get(id)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @PostMapping
    public Mono<ResponseEntity<String>> create(@RequestBody TransactionDto dto) {
        return transactionService.create(dto)
                .flatMap(s -> successResponseCreate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseCreate());
    }

    @PutMapping
    public Mono<ResponseEntity<String>> update(@RequestBody TransactionDto dto) {
        return transactionService.update(dto)
                .flatMap(s -> successResponseUpdate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseUpdate());
    }

    @DeleteMapping
    public Mono<ResponseEntity<String>> delete(@RequestBody TransactionDto dto) {
        return transactionService.delete(dto)
                .flatMap(s -> successResponseDelete())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseDelete());
    }
}

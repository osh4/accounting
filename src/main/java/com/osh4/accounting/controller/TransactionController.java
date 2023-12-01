package com.osh4.accounting.controller;

import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.service.PaginatedSearchService;
import com.osh4.accounting.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transaction")
@Slf4j
@AllArgsConstructor
public class TransactionController extends BaseController {

    private final TransactionService transactionService;
    private final PaginatedSearchService paginatedSearchService;

    @GetMapping
    public Mono<Page<TransactionDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id_asc") String sort) {
        return transactionService.getAll(paginatedSearchService.paginationInfo(page, size, sort));
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

    @PutMapping("/{id}")
    public Mono<ResponseEntity<String>> update(@PathVariable String id, @RequestBody TransactionDto dto) {
        return transactionService.update(id, dto)
                .flatMap(s -> successResponseUpdate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseUpdate());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return transactionService.delete(id)
                .flatMap(s -> successResponseDelete())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseDelete());
    }
}

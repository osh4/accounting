package com.osh4.accounting.controller;

import com.osh4.accounting.dto.TransactionDto;
import com.osh4.accounting.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transactions")
@Slf4j
@AllArgsConstructor
public class TransactionController extends BaseController {

    private final TransactionService transactionService;

    @GetMapping
    public Mono<Page<TransactionDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id_asc") String sort) {
        return transactionService.getAll(paginatedSearchService.paginationInfo(page, size, sort));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TransactionDto>> get(@PathVariable String id) {
        return transactionService.get(id)
                .flatMap(this::successResponse);
    }

    @PostMapping
    public Mono<ResponseEntity<TransactionDto>> create(@RequestBody TransactionDto dto) {
        return transactionService.create(dto)
                .flatMap(this::successResponse);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TransactionDto>> update(@PathVariable String id, @RequestBody TransactionDto dto) {
        return transactionService.update(id, dto)
                .flatMap(this::successResponse);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return transactionService.delete(id)
                .then(Mono.defer(this::successResponseDelete));
    }
}

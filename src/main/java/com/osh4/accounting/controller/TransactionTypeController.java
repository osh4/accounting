package com.osh4.accounting.controller;

import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.service.TransactionTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/transactions/types")
@Slf4j
@AllArgsConstructor
public class TransactionTypeController extends BaseController {

    private final TransactionTypeService transactionTypeService;


    @GetMapping()
    public Mono<Page<TransactionTypeDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id_asc") String sort) {
        return transactionTypeService.getAll(paginatedSearchService.paginationInfo(page, size, sort));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TransactionTypeDto>> get(@PathVariable String id) {
        return transactionTypeService.get(id)
                .flatMap(this::successResponse);
    }

    @PostMapping
    public Mono<ResponseEntity<TransactionTypeDto>> create(@Valid @RequestBody TransactionTypeDto dto) {
        return transactionTypeService.create(dto)
                .flatMap(this::successResponse);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TransactionTypeDto>> update(@PathVariable String id,
                                                           @RequestBody TransactionTypeDto dto) {
        return transactionTypeService.update(id, dto)
                .flatMap(this::successResponse);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return transactionTypeService.delete(id)
                .then(Mono.defer(this::successResponseDelete));
    }
}

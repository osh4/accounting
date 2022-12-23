package com.osh4.accounting.controller;

import com.osh4.accounting.dto.TransactionTypeDto;
import com.osh4.accounting.service.TransactionTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transaction/type")
@Slf4j
@AllArgsConstructor
public class TransactionTypeController extends BaseController {

    private final TransactionTypeService transactionTypeService;


    @GetMapping()
    public Flux<TransactionTypeDto> getAllTypes() {
        return transactionTypeService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TransactionTypeDto>> getType(@PathVariable String id) {
        return transactionTypeService.get(id)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse(null));
    }

    @PostMapping
    public Mono<ResponseEntity<String>> create(@RequestBody TransactionTypeDto dto) {
        return transactionTypeService.create(dto)
                .flatMap(s -> successResponseCreate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseCreate());
    }

    @PutMapping
    public Mono<ResponseEntity<String>> update(@RequestBody TransactionTypeDto dto) {
        return transactionTypeService.update(dto)
                .flatMap(s -> successResponseUpdate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseUpdate());
    }

    @DeleteMapping
    public Mono<ResponseEntity<String>> delete(@RequestBody TransactionTypeDto dto) {
        return transactionTypeService.delete(dto)
                .flatMap(s -> successResponseDelete())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseDelete());
    }
}

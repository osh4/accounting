package com.osh4.accounting.controller;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.service.SettingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/setting")
@Slf4j
@AllArgsConstructor
public class SettingController extends BaseController {

    private final SettingService settingService;

    @GetMapping
    public Mono<Page<SettingDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "key_asc") String sort) {
        return settingService.getAll(paginatedSearchService.paginationInfo(page, size, sort));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<SettingDto>> get(@PathVariable String id) {
        return settingService.get(id)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @PostMapping
    public Mono<ResponseEntity<String>> create(@Valid @RequestBody SettingDto dto) {
        return settingService.create(dto)
                .flatMap(s -> successResponseCreate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseCreate());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<String>> update(@PathVariable String id, @Valid @RequestBody SettingDto dto) {
        return settingService.update(id, dto)
                .flatMap(s -> successResponseUpdate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseUpdate());
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return settingService.delete(id)
                .flatMap(s -> successResponseDelete())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseDelete());
    }

}

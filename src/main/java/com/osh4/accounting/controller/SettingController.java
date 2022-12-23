package com.osh4.accounting.controller;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.service.SettingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/setting")
@Slf4j
@AllArgsConstructor
public class SettingController extends BaseController {

    private final SettingService settingService;

    @GetMapping
    public Flux<SettingDto> getAll() {
        return settingService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<SettingDto>> get(@PathVariable String id) {
        return settingService.get(id)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @PostMapping
    public Mono<ResponseEntity<String>> create(@RequestBody SettingDto dto) {
        return settingService.create(dto)
                .flatMap(s -> successResponseCreate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseCreate());
    }

    @PutMapping
    public Mono<ResponseEntity<String>> update(@RequestBody SettingDto dto) {
        return settingService.update(dto)
                .flatMap(s -> successResponseUpdate())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseUpdate());
    }


    @DeleteMapping
    public Mono<ResponseEntity<String>> delete(@RequestBody SettingDto dto) {
        return settingService.delete(dto)
                .flatMap(s -> successResponseDelete())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseDelete());
    }

}

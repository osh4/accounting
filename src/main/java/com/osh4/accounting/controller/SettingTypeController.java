package com.osh4.accounting.controller;

import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.service.SettingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/setting/type")
@Slf4j
@AllArgsConstructor
public class SettingTypeController extends BaseController {
    private final SettingService settingService;

    @GetMapping("/{name}")
    public Mono<ResponseEntity<SettingTypeDto>> get(@PathVariable String name) {
        return settingService.getType(name)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @GetMapping()
    public Flux<SettingTypeDto> getAllTypes() {
        return settingService.getAllTypes();
    }
}

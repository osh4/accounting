package com.osh4.accounting.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.service.SettingsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/setting")
@Slf4j
@AllArgsConstructor
public class SettingsController {
    private final SettingsService settingsService;

    @GetMapping
    public Flux<SettingDto> getSettings() {
        return settingsService.getAllSettings();
    }

    @GetMapping("/types")
    public Flux<String> getSettingTypes() {
        return settingsService.getAllTypes();
    }

    @PostMapping
    public Mono<ResponseEntity<String>> createSetting(@RequestBody SettingDto settingDto) {
        return settingsService.create(settingDto)
                .flatMap(s -> Mono.just(ResponseEntity.accepted().contentType(MediaType.APPLICATION_JSON).body(s)))
                .onErrorReturn(ResponseEntity.unprocessableEntity().body("Can't add the setting"));
    }

    @PutMapping
    public Mono<ResponseEntity<String>> updateSetting(@RequestBody SettingDto settingDto) {
        return settingsService.update(settingDto)
                .flatMap(s -> Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(s)))
                .onErrorReturn(ResponseEntity.unprocessableEntity().body("Can't update the setting"));
    }

    @ResponseBody
    @DeleteMapping
    public Mono<ResponseEntity<String>> deleteSetting(@RequestBody SettingDto settingDto) {
        return settingsService.delete(settingDto)
                .flatMap(s -> Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(s)))
                .onErrorReturn(ResponseEntity.unprocessableEntity().body("Can't remove the setting"));
    }
}

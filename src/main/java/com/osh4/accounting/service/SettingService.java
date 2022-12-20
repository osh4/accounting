package com.osh4.accounting.service;

import com.osh4.accounting.dto.SettingDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface SettingService {
    Flux<SettingDto> getAll();

    Mono<SettingDto> getByKey(String key);

    Mono<String> create(SettingDto settingDto);

    Mono<String> update(SettingDto settingDto);

    Mono<String> delete(SettingDto settingDto);

    Flux<String> getAllTypes();
}

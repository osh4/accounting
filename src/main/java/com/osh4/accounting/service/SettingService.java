package com.osh4.accounting.service;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface SettingService {
    Flux<SettingDto> getAll();

    Mono<SettingDto> get(String key);

    Mono<Setting> create(SettingDto settingDto);

    Mono<Void> update(SettingDto settingDto);

    Mono<Void> delete(SettingDto settingDto);

    Flux<SettingTypeDto> getAllTypes();

    Mono<SettingTypeDto> getType(String name);
}

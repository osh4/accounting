package com.osh4.accounting.service;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface SettingService {
    Mono<Page<SettingDto>> getAll(PageRequest pageRequest);

    Mono<SettingDto> get(String key);

    Mono<Setting> create(SettingDto settingDto);

    Mono<Void> update(String id, SettingDto settingDto);

    Mono<Void> delete(String id);

    Flux<SettingTypeDto> getAllTypes();

    Mono<SettingTypeDto> getType(String name);
}

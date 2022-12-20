package com.osh4.accounting.converters.impl;

import com.osh4.accounting.persistance.r2dbc.Setting;
import org.springframework.stereotype.Service;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingDto;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
public class SettingReverseConverter implements Converter<SettingDto, Mono<Setting>> {
    @Override
    public Mono<Setting> convert(SettingDto settings) {
        return Mono.just(Setting.builder()
                .key(settings.getKey())
                .type(settings.getType())
                .value(settings.getValue())
                .build());
    }
}

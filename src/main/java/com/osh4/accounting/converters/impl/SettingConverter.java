package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
public class SettingConverter implements Converter<Setting, SettingDto> {
    @Override
    public SettingDto convert(Setting model) {
        return isNull(model) ? null : SettingDto.builder()
                .key(model.getKey())
                .type(model.getType())
                .value(model.getValue())
                .build();
    }
}

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
public class SettingReverseConverter implements Converter<SettingDto, Setting> {
    @Override
    public Setting convert(SettingDto dto) {
        return isNull(dto) ? null : Setting.builder()
                .key(dto.getKey())
                .type(dto.getType())
                .value(dto.getValue())
                .build();
    }
}

package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import org.springframework.stereotype.Service;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
public class SettingReverseConverter implements Converter<SettingDto, Setting> {
    @Override
    public Setting convert(SettingDto settings) {
        return Setting.builder()
                .key(settings.getKey())
                .type(settings.getType())
                .value(settings.getValue())
                .build();
    }
}

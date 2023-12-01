package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.SettingType;
import org.springframework.stereotype.Service;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
public class SettingTypeConverter implements Converter<SettingType, SettingTypeDto> {

    @Override
    public SettingTypeDto convertInternal(SettingType model) {
        return SettingTypeDto.builder()
                .name(model.getName())
                .className(model.getClassName())
                .build();
    }
}

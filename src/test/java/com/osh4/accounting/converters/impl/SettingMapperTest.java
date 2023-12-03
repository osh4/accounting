package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import com.osh4.accounting.persistance.r2dbc.SettingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingMapperTest {

    private static final String KEY = "key";
    private static final String VALUE = "value";

    @Mock
    private Setting settings;
    @Mock
    private SettingType settingType;
    @Mock
    private SettingTypeDto settingTypeDto;
    @Mock
    private SettingDto settingDto;
    @Mock
    private SettingTypeMapperImpl settingTypeMapper;
    @InjectMocks
    private SettingMapperImpl settingMapper;

    @Test
    public void shouldPopulateFields() {
        when(settingTypeMapper.toDto(settingType)).thenReturn(settingTypeDto);
        when(settings.getKey()).thenReturn(KEY);
        when(settings.getSettingType()).thenReturn(settingType);
        when(settings.getValue()).thenReturn(VALUE);

        SettingDto result = settingMapper.toDto(settings);
        assertEquals(KEY, result.getKey());
        assertEquals(settingTypeDto, result.getSettingType());
        assertEquals(VALUE, result.getValue());
    }

    @Test
    public void shouldReversePopulateFields() {
        when(settingTypeMapper.toModel(settingTypeDto)).thenReturn(settingType);
        when(settingDto.getKey()).thenReturn(KEY);
        when(settingDto.getSettingType()).thenReturn(settingTypeDto);
        when(settingDto.getValue()).thenReturn(VALUE);

        Setting result = settingMapper.toModel(settingDto);
        assertEquals(KEY, result.getKey());
        assertEquals(settingType, result.getSettingType());
        assertEquals(VALUE, result.getValue());
    }
}
package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
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
    private static final String SETTING_TYPE_ID = "settingTypeId";

    @Mock
    private Setting settings;
    @Mock
    private SettingTypeDto settingTypeDto;
    @Mock
    private SettingDto settingDto;
    @InjectMocks
    private SettingMapperImpl settingMapper;

    @Test
    public void shouldPopulateFields() {
        when(settings.getKey()).thenReturn(KEY);
        when(settings.getSettingTypeId()).thenReturn(SETTING_TYPE_ID);
        when(settings.getValue()).thenReturn(VALUE);

        SettingDto result = settingMapper.toDto(settings);

        assertEquals(KEY, result.getKey());
        assertEquals(SETTING_TYPE_ID, result.getSettingType().getId());
        assertEquals(VALUE, result.getValue());
    }

    @Test
    public void shouldReversePopulateFields() {
        when(settingTypeDto.getId()).thenReturn(SETTING_TYPE_ID);
        when(settingDto.getKey()).thenReturn(KEY);
        when(settingDto.getSettingType()).thenReturn(settingTypeDto);
        when(settingDto.getValue()).thenReturn(VALUE);

        Setting result = settingMapper.toModel(settingDto);

        assertEquals(KEY, result.getKey());
        assertEquals(SETTING_TYPE_ID, result.getSettingTypeId());
        assertEquals(VALUE, result.getValue());
    }
}
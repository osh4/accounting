package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingReverseConverterTest {
    private static final String KEY = "key";
    private static final String TYPE = "type";
    private static final String VALUE = "value";

    @Mock
    private SettingDto settingDto;

    @InjectMocks
    private SettingReverseConverter converter;

    @Test
    public void shouldPopulateFields() {
        when(settingDto.getKey()).thenReturn(KEY);
        when(settingDto.getType()).thenReturn(TYPE);
        when(settingDto.getValue()).thenReturn(VALUE);

        Setting result = converter.convert(settingDto);
        assertEquals(KEY, result.getKey());
        assertEquals(TYPE, result.getType());
        assertEquals(VALUE, result.getValue());
    }
}
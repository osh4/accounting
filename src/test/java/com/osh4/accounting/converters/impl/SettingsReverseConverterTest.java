package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.SettingsDto;
import com.osh4.accounting.persistance.entity.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingsReverseConverterTest {
    private static final String GROUP = "group";
    private static final String KEY = "key";
    private static final String TYPE = "type";
    private static final String VALUE = "value";

    @Mock
    private SettingsDto settingsDto;

    @InjectMocks
    private SettingsReverseConverter converter;

    @Test
    public void shouldPopulateFields() {
        when(settingsDto.getGroup()).thenReturn(GROUP);
        when(settingsDto.getKey()).thenReturn(KEY);
        when(settingsDto.getType()).thenReturn(TYPE);
        when(settingsDto.getValue()).thenReturn(VALUE);

        Settings result = converter.convert(settingsDto);
        assertEquals(GROUP, result.getGrp());
        assertEquals(KEY, result.getKey());
        assertEquals(TYPE, result.getType());
        assertEquals(VALUE, result.getValue());
    }
}
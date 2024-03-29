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
class SettingsConverterTest {

    private static final String GROUP = "group";
    private static final String KEY = "key";
    private static final String TYPE = "type";
    private static final String VALUE = "value";

    @Mock
    private Settings settings;

    @InjectMocks
    private SettingsConverter settingsConverter;

    @Test
    public void shouldPopulateFields() {
        when(settings.getGrp()).thenReturn(GROUP);
        when(settings.getKey()).thenReturn(KEY);
        when(settings.getType()).thenReturn(TYPE);
        when(settings.getValue()).thenReturn(VALUE);

        SettingsDto result = settingsConverter.convert(settings);
        assertEquals(GROUP, result.getGroup());
        assertEquals(KEY, result.getKey());
        assertEquals(TYPE, result.getType());
        assertEquals(VALUE, result.getValue());
    }
}
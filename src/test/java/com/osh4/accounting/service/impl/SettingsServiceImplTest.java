package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingsDto;
import com.osh4.accounting.persistance.entity.Settings;
import com.osh4.accounting.persistance.repository.SettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettingsServiceImplTest {
    private static final String GROUP = "group";
    private static final String KEY = "key";
    private static final String OLD_VALUE = "oldValue";
    private static final String NEW_VALUE = "newValue";

    private static final List<String> ALL_TYPES = List.of(String.class.getName(), Long.class.getName(), Boolean.class.getName());


    @Mock
    private Settings settings;
    @Mock
    private Settings oldSettings;
    @Mock
    private SettingsDto settingsDto;

    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private Converter<Settings, SettingsDto> settingsConverter;
    @Mock
    private Converter<SettingsDto, Settings> settingsReverseConverter;
    @InjectMocks
    private SettingsServiceImpl service;


    @BeforeEach
    public void setUp() {
        Mockito.lenient().when(settingsDto.getGroup()).thenReturn(GROUP);
        Mockito.lenient().when(settingsDto.getKey()).thenReturn(KEY);
        Mockito.lenient().when(settingsDto.getValue()).thenReturn(NEW_VALUE);
        Mockito.lenient().when(oldSettings.getValue()).thenReturn(OLD_VALUE);
        Mockito.lenient().when(settings.getValue()).thenReturn(NEW_VALUE);
        Mockito.lenient().when(settingsRepository.findAll()).thenReturn(Collections.singletonList(settings));
        Mockito.lenient().when(settingsConverter.convertAll(any(List.class))).thenReturn(Collections.singletonList(settingsDto));
        Mockito.lenient().when(settingsReverseConverter.convertAll(any(List.class)))
                .thenReturn(Collections.singletonList(settings));
        Mockito.lenient().when(settingsReverseConverter.convert(any(SettingsDto.class))).thenReturn(settings);
        Mockito.lenient().when(settingsRepository.save(any(Settings.class))).thenReturn(settings);
        Mockito.lenient().when(settingsRepository.findByGrpAndKey(GROUP, KEY)).thenReturn(settings);

    }

    @Test
    public void shouldGetAndConvertAllSettings() {
        List<SettingsDto> result = service.getAllSettings();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void shouldReturnEmptyListIfNoSettings() {
        Mockito.lenient().when(settingsRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.lenient().when(settingsConverter.convertAll(Collections.emptyList()))
                .thenReturn(Collections.emptyList());
        List<SettingsDto> result = service.getAllSettings();
        assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test
    public void shouldUpdateSettings() {
        Mockito.lenient().when(settingsRepository.findByGrpAndKey(GROUP, KEY)).thenReturn(oldSettings);

        service.update(settingsDto);

        verify(oldSettings).setValue(NEW_VALUE);
    }

    @Test
    public void shouldNotUpdateSettingsIfEqualValues() {

        service.update(settingsDto);
        verify(settings, times(0)).setValue(NEW_VALUE);
    }

    @Test
    public void shouldDeleteSettings() {

        service.delete(settingsDto);

        verify(settingsRepository, times(1)).delete(settings);
    }

    @Test
    public void shouldDoNothingIfSettingNotExists() {
        Mockito.lenient().when(settingsRepository.findByGrpAndKey(GROUP, KEY)).thenReturn(null);

        service.delete(settingsDto);

        verify(settingsRepository, times(0)).delete(settings);
    }

    @Test
    public void shouldReturnAllSettingTypes() {
        List<String> result = service.getAllTypes();
        assertTrue(ALL_TYPES.containsAll(result));
    }
//
//    @Test
//    public void shouldCreateNewSettings() {
//        Mockito.lenient().when(settingsRepository.findByGrpAndKey(GROUP, KEY)).thenReturn(settings);
//
//        service.create(settingsDto);
//
//        verify(settingsRepository, times(1)).save(settings);
//    }

}
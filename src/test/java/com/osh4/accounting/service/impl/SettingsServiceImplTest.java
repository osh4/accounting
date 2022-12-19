package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import com.osh4.accounting.persistance.repository.SettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SettingsServiceImplTest {
    private static final String KEY = "key";
    private static final String OLD_VALUE = "oldValue";
    private static final String NEW_VALUE = "newValue";

    @Mock
    private Setting settings;
    @Mock
    private Setting oldSettings;
    @Mock
    private SettingDto settingDto;

    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private Converter<Setting, SettingDto> settingsConverter;
    @Mock
    private Converter<SettingDto, Setting> settingsReverseConverter;
    @InjectMocks
    private SettingsServiceImpl service;


    @BeforeEach
    public void setUp() {
        Mockito.lenient().when(settingDto.getKey()).thenReturn(KEY);
        Mockito.lenient().when(settingDto.getValue()).thenReturn(NEW_VALUE);
        Mockito.lenient().when(oldSettings.getValue()).thenReturn(OLD_VALUE);
        Mockito.lenient().when(settings.getValue()).thenReturn(NEW_VALUE);
        Mockito.lenient().when(settingsRepository.findAll()).thenReturn(Flux.just(settings));
        Mockito.lenient().when(settingsConverter.convertAll(any())).thenReturn(Collections.singletonList(settingDto));
        Mockito.lenient().when(settingsReverseConverter.convertAll(any()))
                .thenReturn(Collections.singletonList(settings));
        Mockito.lenient().when(settingsReverseConverter.convert(any(SettingDto.class))).thenReturn(settings);
        Mockito.lenient().when(settingsRepository.save(any(Setting.class))).thenReturn(Mono.just(settings));
        Mockito.lenient().when(settingsRepository.findByKey(KEY)).thenReturn(Mono.just(settings));

    }

    @Test
    public void shouldGetAndConvertAllSettings() {
        Flux<SettingDto> result = service.getAllSettings();
        assertNotNull(result);
        assertTrue(false);
        //assertEquals(1, result.size());
    }

    @Test
    public void shouldReturnEmptyListIfNoSettings() {
        Mockito.lenient().when(settingsRepository.findAll()).thenReturn(Flux.empty());
        Mockito.lenient().when(settingsConverter.convertAll(Collections.emptyList())).thenReturn(Collections.emptyList());
        Flux<SettingDto> result = service.getAllSettings();
        //result.hasElements().flatMap(Assertions::assertFalse).subscribe();
        //assertTrue(CollectionUtils.isEmpty(result));
        assertTrue(false);
    }

    @Test
    public void shouldUpdateSettings() {
        Mockito.lenient().when(settingsRepository.findByKey(KEY)).thenReturn(Mono.just(oldSettings));

        service.update(settingDto);

        verify(oldSettings).setValue(NEW_VALUE);
    }

    @Test
    public void shouldNotUpdateSettingsIfEqualValues() {

        service.update(settingDto);
        verify(settings, times(0)).setValue(NEW_VALUE);
    }

    @Test
    public void shouldDeleteSettings() {

        service.delete(settingDto);

        verify(settingsRepository, times(1)).delete(settings);
    }

    @Test
    public void shouldDoNothingIfSettingNotExists() {
        Mockito.lenient().when(settingsRepository.findByKey(KEY)).thenReturn(null);

        service.delete(settingDto);

        verify(settingsRepository, times(0)).delete(settings);
    }

}
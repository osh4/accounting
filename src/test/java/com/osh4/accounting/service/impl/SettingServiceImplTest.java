package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.SettingMapper;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import com.osh4.accounting.persistance.repository.SettingRepository;
import com.osh4.accounting.persistance.repository.SettingTypeRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettingServiceImplTest {
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
    private PageRequest pageRequest;

    @Mock
    private SettingRepository settingRepository;
    @Mock
    private SettingTypeRepository settingTypeRepository;
    @Mock
    private SettingMapper settingMapper;
    @InjectMocks
    private SettingServiceImpl service;

//        Mockito.lenient().when(settingsReverseConverter.convert(any(SettingDto.class))).thenReturn(settings);// for CREATE test

    @Test
    @Disabled
    public void shouldGetAndConvertAllSettings() {
        // given
        when(settingRepository.findAllBy(pageRequest)).thenReturn(Flux.just(settings));
        when(settingMapper.toDto(settings)).thenReturn(settingDto);

        // when
        Page<SettingDto> result = service.getAll(pageRequest).block();

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(settingDto);
    }

    @Test
    public void shouldReturnEmptyListIfNoSettings() {
        // given
        when(settingRepository.findAllBy(pageRequest)).thenReturn(Flux.empty());
        // when
        var result = service.getAll(pageRequest).block();
        // then
        assertThat(result.getContent()).hasSize(0);
    }

    @Test
    public void shouldUpdateSettings() {
        // given
        when(oldSettings.getValue()).thenReturn(OLD_VALUE);
        when(settingRepository.findById(KEY)).thenReturn(Mono.just(oldSettings));
        when(settingRepository.save(any(Setting.class))).thenReturn(Mono.just(settings));
        when(settingDto.getValue()).thenReturn(NEW_VALUE);

        // when
        service.update(KEY, settingDto).block();

        // then
        verify(oldSettings).setValue(NEW_VALUE);
        verify(settingRepository).save(oldSettings);
    }

    @Test
    public void shouldNotUpdateSettingsIfEqualValues() {
        when(settingRepository.findById(KEY)).thenReturn(Mono.just(settings));
        when(settingRepository.save(any(Setting.class))).thenReturn(Mono.just(settings));
        service.update(KEY, settingDto).block();
        verify(settings, times(0)).setValue(NEW_VALUE);
    }

    @Test
    public void shouldDeleteSettings() {
        // when
        service.delete(KEY);
        // then
        verify(settingRepository, times(1)).deleteById(KEY);
    }
}
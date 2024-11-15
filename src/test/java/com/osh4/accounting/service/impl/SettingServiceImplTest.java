package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.SettingMapper;
import com.osh4.accounting.converters.impl.SettingTypeMapper;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import com.osh4.accounting.persistance.r2dbc.SettingType;
import com.osh4.accounting.persistance.repository.SettingRepository;
import com.osh4.accounting.persistance.repository.SettingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettingServiceImplTest {
    private static final String KEY = "key";
    private static final String OLD_VALUE = "oldValue";
    private static final String OLD_KEY = "oldKey";
    private static final String NEW_KEY = "newKey";
    private static final String OLD_SETTING_TYPE_ID = "oldSettingTypeId";
    private static final String NEW_SETTING_TYPE_ID = "newSettingTypeId";
    private static final String NEW_VALUE = "newValue";
    private static final String SETTING_TYPE_ID = "settingTypeId";
    private static final Long RECORDS_COUNT = 10L;
    private static final Sort DESC_SETTING_TYPE_ID_SORT = Sort.by("settingTypeId").descending();
    private static final Sort ASC_SETTING_TYPE_ID_SORT = Sort.by("settingTypeId").ascending();
    private static final Sort.Order ASC_SETTING_TYPE_SORT_ORDER = Sort.Order.asc("settingType");

    @Mock
    private Setting setting;
    @Mock
    private SettingType settingType;
    @Mock
    private SettingTypeDto settingTypeDto;
    @Mock
    private Setting oldSettings;
    @Mock
    private SettingDto settingDto;
    @Mock
    private PageRequest pageRequest;
    @Mock
    private Sort sort;

    @Mock
    private SettingRepository settingRepository;
    @Mock
    private SettingTypeRepository settingTypeRepository;
    @Mock
    private SettingTypeMapper settingTypeMapper;
    @Mock
    private SettingMapper settingMapper;
    @InjectMocks
    private SettingServiceImpl service;

    @Test
    public void shouldGetSettingById() {
        // given
        when(settingRepository.findById(KEY)).thenReturn(Mono.just(setting));
        when(settingMapper.toDto(setting)).thenReturn(settingDto);

        // when
        SettingDto result = service.get(KEY).block();

        // then
        assertEquals(settingDto, result);
    }

    @Test
    public void shouldCreateSettingFromDto() {
        // given
        when(settingRepository.save(any(Setting.class))).thenReturn(Mono.just(setting));
        when(settingMapper.toModel(settingDto)).thenReturn(setting);
        when(setting.setAsNew()).thenReturn(setting);
        when(settingMapper.toDto(setting)).thenReturn(settingDto);

        // when
        SettingDto result = service.create(settingDto).block();

        // then
        verify(settingRepository).save(setting);
        assertEquals(settingDto, result);
    }

    @Test
    public void shouldGetAndConvertAllSettings() {
        // given
        when(settingRepository.findAllBy(pageRequest)).thenReturn(Flux.just(setting));
        when(settingMapper.toDto(setting)).thenReturn(settingDto);
        when(settingDto.getSettingType()).thenReturn(settingTypeDto);
        when(settingTypeDto.getId()).thenReturn(SETTING_TYPE_ID);
        when(settingRepository.count()).thenReturn(Mono.just(RECORDS_COUNT));
        when(settingTypeRepository.findById(SETTING_TYPE_ID)).thenReturn(Mono.just(settingType));
        when(settingTypeMapper.toDto(settingType)).thenReturn(settingTypeDto);

        // when
        Page<SettingDto> result = service.getAll(pageRequest).block();

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1)
                .contains(settingDto);
        assertThat(result.getContent().get(0).getSettingType()).isNotNull().isEqualTo(settingTypeDto);
    }

    @Test
    public void shouldUseDescSortWhenConvertAllSettings() {
        // given
        when(pageRequest.getSort()).thenReturn(sort);
        when(sort.stream()).thenReturn(Stream.of(Sort.Order.desc("settingType")));
        when(settingRepository.findAllBy(pageRequest)).thenReturn(Flux.empty());
        when(settingRepository.count()).thenReturn(Mono.just(RECORDS_COUNT));

        // when
        service.getAll(pageRequest).block();

        // then
        verify(pageRequest).withSort(DESC_SETTING_TYPE_ID_SORT);
    }

    @Test
    public void shouldUseAscSortWhenConvertAllSettings() {
        // given
        when(pageRequest.getSort()).thenReturn(sort);
        when(sort.stream()).thenReturn(Stream.of(Sort.Order.asc("settingType")));
        when(settingRepository.findAllBy(pageRequest)).thenReturn(Flux.empty());
        when(settingRepository.count()).thenReturn(Mono.just(RECORDS_COUNT));
        when(sort.getOrderFor("settingType")).thenReturn(ASC_SETTING_TYPE_SORT_ORDER);

        // when
        service.getAll(pageRequest).block();

        // then
        verify(pageRequest).withSort(ASC_SETTING_TYPE_ID_SORT);
    }

    @Test
    public void shouldReturnEmptyListIfNoSettings() {
        // given
        when(settingRepository.findAllBy(pageRequest)).thenReturn(Flux.empty());
        when(settingRepository.count()).thenReturn(Mono.just(RECORDS_COUNT));

        // when
        var result = service.getAll(pageRequest).block();

        // then
        assertNotNull(result);
        assertThat(result.getContent()).hasSize(0);
    }

    @Test
    public void shouldUpdateSettings() {
        // given
        when(oldSettings.getValue()).thenReturn(OLD_VALUE);
        when(oldSettings.getKey()).thenReturn(OLD_KEY);
        when(oldSettings.getSettingTypeId()).thenReturn(OLD_SETTING_TYPE_ID);
        when(settingRepository.findById(KEY)).thenReturn(Mono.just(oldSettings));
        when(settingRepository.save(any(Setting.class))).thenReturn(Mono.just(setting));
        when(settingDto.getValue()).thenReturn(NEW_VALUE);
        when(settingDto.getKey()).thenReturn(NEW_KEY);
        when(settingDto.getSettingType()).thenReturn(settingTypeDto);
        when(settingTypeDto.getId()).thenReturn(NEW_SETTING_TYPE_ID);
        when(settingMapper.toDto(setting)).thenReturn(settingDto);

        // when
        service.update(KEY, settingDto).block();

        // then
        verify(oldSettings).setValue(NEW_VALUE);
        verify(oldSettings).setKey(NEW_KEY);
        verify(oldSettings).setSettingTypeId(NEW_SETTING_TYPE_ID);
        verify(settingRepository).save(oldSettings);
    }

    @Test
    public void shouldNotUpdateSettingsIfEqualValues() {
        // given
        when(oldSettings.getValue()).thenReturn(NEW_VALUE);
        when(oldSettings.getKey()).thenReturn(NEW_KEY);
        when(oldSettings.getSettingTypeId()).thenReturn(NEW_SETTING_TYPE_ID);
        when(settingRepository.findById(KEY)).thenReturn(Mono.just(oldSettings));
        when(settingRepository.save(any(Setting.class))).thenReturn(Mono.just(setting));
        when(settingDto.getValue()).thenReturn(NEW_VALUE);
        when(settingDto.getKey()).thenReturn(NEW_KEY);
        when(settingDto.getSettingType()).thenReturn(settingTypeDto);
        when(settingTypeDto.getId()).thenReturn(NEW_SETTING_TYPE_ID);
        when(settingMapper.toDto(setting)).thenReturn(settingDto);

        // when
        service.update(KEY, settingDto).block();

        // then
        verify(setting, never()).setValue(NEW_VALUE);
    }

    @Test
    public void shouldNotUpdateSettingIfDtoIsNull() {
        // given
        when(settingRepository.findById(KEY)).thenReturn(Mono.just(setting));
        when(settingMapper.toDto(setting)).thenReturn(settingDto);

        // when
        SettingDto result = service.update(KEY, null).block();

        // then
        verify(setting, never()).setValue(NEW_VALUE);
        verify(settingRepository, never()).save(setting);
        assertEquals(settingDto, result);
    }

    @Test
    public void shouldDeleteSettings() {
        // when
        service.delete(KEY);

        // then
        verify(settingRepository, times(1)).deleteById(KEY);
    }

    @Test
    public void shouldGetSettingType() {
        // given
        when(settingTypeRepository.findById(SETTING_TYPE_ID)).thenReturn(Mono.just(settingType));
        when(settingTypeMapper.toDto(settingType)).thenReturn(settingTypeDto);

        // when
        SettingTypeDto result = service.getType(SETTING_TYPE_ID).block();

        // then
        assertThat(result).isEqualTo(settingTypeDto);
    }

    @Test
    public void shouldGetAllSettingTypes() {
        // given
        when(settingTypeRepository.findAll()).thenReturn(Flux.just(settingType));
        when(settingTypeMapper.toDto(settingType)).thenReturn(settingTypeDto);

        // when
        SettingTypeDto result = service.getAllTypes().blockFirst();

        // then
        assertThat(result).isEqualTo(settingTypeDto);
    }
}
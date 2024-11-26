package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.SettingMapper;
import com.osh4.accounting.converters.impl.SettingTypeMapper;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.exception.AlreadyExistsException;
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
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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
    private static final String SETTING_TYPE_NAME = "settingTypeName";
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
        var result = service.get(KEY);

        // then
        StepVerifier.create(result).expectNext(settingDto).expectComplete().verify();
    }

    @Test
    public void shouldNotCreateSettingIfExist() {
        // given
        when(settingRepository.findById(KEY)).thenReturn(Mono.just(setting));
        when(settingMapper.toModel(settingDto)).thenReturn(setting);
        when(setting.setAsNew()).thenReturn(setting);
        when(settingDto.getKey()).thenReturn(KEY);

        // when
        Mono<SettingDto> result = service.create(settingDto);

        StepVerifier.create(result)
                .expectError(AlreadyExistsException.class)
                .verify();
    }

    @Test
    public void shouldCreateSettingFromDto() {
        // given
        when(settingRepository.findById(KEY)).thenReturn(Mono.empty());
        when(settingRepository.save(any(Setting.class))).thenReturn(Mono.just(setting));
        when(settingMapper.toModel(settingDto)).thenReturn(setting);
        when(setting.setAsNew()).thenReturn(setting);
        when(setting.isNewEntity()).thenReturn(true);
        when(settingMapper.toDto(setting)).thenReturn(settingDto);
        when(settingDto.getKey()).thenReturn(KEY);

        // when
        Mono<SettingDto> result = service.create(settingDto);

        // then
        StepVerifier.create(result)
                .expectNext(settingDto)
                .verifyComplete();
        verify(settingRepository).save(setting);
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
        Mono<Page<SettingDto>> result = service.getAll(pageRequest);

        // then
        StepVerifier.create(result)
                .expectNextMatches(this::isExpectedSettingWithType)
                .expectComplete()
                .verify();
    }

    private boolean isExpectedSettingWithType(Page<SettingDto> res) {
        return res.getContent().size() == 1 &&
                res.getContent().contains(settingDto) &&
                Objects.equals(res.getContent().get(0).getSettingType(), settingTypeDto);
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
        var result = service.getAll(pageRequest);

        // then
        StepVerifier.create(result).expectNextMatches(res -> res.getContent().isEmpty()).expectComplete().verify();
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
        var result = service.update(KEY, null);

        // then
        StepVerifier.create(result).expectNext(settingDto).expectComplete().verify();
        verify(setting, never()).setValue(NEW_VALUE);
        verify(settingRepository, never()).save(setting);
    }

    @Test
    public void shouldDeleteSettings() {
        //given
        when(settingRepository.findById(KEY)).thenReturn(Mono.just(setting));
        when(setting.getId()).thenReturn(KEY);
        when(settingRepository.deleteById(KEY)).thenReturn(Mono.empty());
        // when
        service.delete(KEY).block();

        // then
        verify(settingRepository).deleteById(KEY);
    }

    @Test
    public void shouldGetSettingType() {
        // given
        when(settingTypeRepository.findByName(SETTING_TYPE_NAME)).thenReturn(Mono.just(settingType));
        when(settingTypeMapper.toDto(settingType)).thenReturn(settingTypeDto);

        // when
        SettingTypeDto result = service.getType(SETTING_TYPE_NAME).block();

        // then
        assertThat(result).isEqualTo(settingTypeDto);
    }

    @Test
    public void shouldGetAllSettingTypes() {
        // given
        when(settingTypeRepository.findAll()).thenReturn(Flux.just(settingType));
        when(settingTypeMapper.toDto(settingType)).thenReturn(settingTypeDto);

        // when
        var result = service.getAllTypes();

        // then
        StepVerifier.create(result).expectNext(settingTypeDto).expectComplete().verify();
    }
}
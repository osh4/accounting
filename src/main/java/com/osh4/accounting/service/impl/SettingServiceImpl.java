package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.SettingMapper;
import com.osh4.accounting.converters.impl.SettingTypeMapper;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.exception.AlreadyExistsException;
import com.osh4.accounting.exception.NotFoundException;
import com.osh4.accounting.persistance.r2dbc.Setting;
import com.osh4.accounting.persistance.repository.SettingRepository;
import com.osh4.accounting.persistance.repository.SettingTypeRepository;
import com.osh4.accounting.service.SettingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
@Slf4j
@AllArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final SettingRepository repository;
    private final SettingTypeRepository settingTypeRepository;
    private SettingTypeMapper settingTypeMapper;
    private SettingMapper mapper;

    @Override
    public Mono<Page<SettingDto>> getAll(PageRequest pageRequest) {
        Sort sort = pageRequest.getSort();
        if (nonNull(sort) && sort.stream().anyMatch(x -> "settingType".equals(x.getProperty()))) {
            pageRequest.withSort(createSortWithOrdering(sort));
        }
        return repository.findAllBy(pageRequest)
                .map(mapper::toDto)
                .flatMap(this::populateSettingType)
                .collectList()
                .zipWith(repository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    private static Sort createSortWithOrdering(Sort sort) {
        Sort.Order oldSortOrder = sort.getOrderFor("settingType");
        if (oldSortOrder != null && Sort.Direction.ASC.equals(oldSortOrder.getDirection())) {
            return Sort.by("settingTypeId").ascending();
        } else {
            return Sort.by("settingTypeId").descending();
        }
    }

    private Mono<SettingDto> populateSettingType(SettingDto dto) {
        return Mono.justOrEmpty(dto.getSettingType())
                .filter(Objects::nonNull)
                .map(SettingTypeDto::getId)
                .filter(StringUtils::isNotBlank)
                .flatMap(settingTypeRepository::findById)
                .map(settingTypeMapper::toDto)
                .map(settingType -> {
                    dto.setSettingType(settingType);
                    return dto;
                })
                .switchIfEmpty(Mono.just(dto))
                .onErrorReturn(dto);
    }

    @Override
    public Mono<SettingDto> get(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .flatMap(this::populateSettingType)
                .switchIfEmpty(Mono.error(new NotFoundException("Setting not found")));
    }

    @Override
    public Mono<SettingDto> get(String id, String defaultValue) {
        return repository.findById(id)
                .map(mapper::toDto)
                .flatMap(this::populateSettingType)
                .switchIfEmpty(Mono.just(SettingDto.builder().key(id).value(defaultValue).build()));
    }

    @Override
    @Transactional
    public Mono<SettingDto> create(SettingDto dto) {
        return repository.findById(dto.getKey())
                .switchIfEmpty(Mono.just(mapper.toModel(dto).setAsNew()).flatMap(repository::save))
                .filter(Setting::isNewEntity)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(AlreadyExistsException.fromSettingId(dto.getKey())));
    }

    @Override
    @Transactional
    public Mono<SettingDto> update(String id, SettingDto dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.fromSettingId(id)))
                .flatMap(setting -> updateFields(dto, setting))
                .map(mapper::toDto);
    }

    private Mono<Setting> updateFields(SettingDto dto, Setting model) {
        if (isNull(dto)) {
            return Mono.just(model);
        }
        if (isNotBlank(dto.getKey()) && ObjectUtils.notEqual(model.getKey(), dto.getKey())) {
            model.setKey(dto.getKey());
        }
        if (isNotBlank(dto.getValue()) && ObjectUtils.notEqual(model.getValue(), dto.getValue())) {
            model.setValue(dto.getValue());
        }
        if (nonNull(dto.getSettingType()) && isNotBlank(dto.getSettingType().getId()) && ObjectUtils.notEqual(model.getSettingTypeId(), dto.getSettingType().getId())) {
            model.setSettingTypeId(dto.getSettingType().getId());
        }
        return repository.save(model);
    }

    @Override
    @Transactional
    public Mono<Void> delete(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(NotFoundException.fromSettingId(id)))
                .flatMap(currency -> repository.deleteById(currency.getId()));
    }

    @Override
    public Mono<SettingTypeDto> getType(String name) {
        return settingTypeRepository.findByName(name)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorResume(it -> Mono.empty())
                .map(settingTypeMapper::toDto)
                .switchIfEmpty(Mono.error(NotFoundException.fromSettingTypeName(name)));
    }

    @Override
    public Flux<SettingTypeDto> getAllTypes() {
        return settingTypeRepository.findAll().map(settingTypeMapper::toDto);
    }
}

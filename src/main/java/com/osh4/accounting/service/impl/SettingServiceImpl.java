package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.SettingMapper;
import com.osh4.accounting.converters.impl.SettingTypeMapper;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import com.osh4.accounting.persistance.repository.SettingRepository;
import com.osh4.accounting.persistance.repository.SettingTypeRepository;
import com.osh4.accounting.service.SettingService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Service
@AllArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;
    private final SettingTypeRepository settingTypeRepository;
    private SettingTypeMapper settingTypeMapper;
    private SettingMapper settingMapper;

    @Override
    public Mono<Page<SettingDto>> getAll(PageRequest pageRequest) {
        return settingRepository.findAllBy(pageRequest)
                .map(settingMapper::toDto)
                .collectList()
                .map(t -> new PageImpl<>(t, pageRequest, t.size()));
    }

    @Override
    public Mono<SettingDto> get(String id) {
        return settingRepository.findById(id)
                .map(settingMapper::toDto)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    @Transactional
    public Mono<Setting> create(SettingDto dto) {
        return settingRepository.save(settingMapper.toModel(dto).setAsNew());
    }

    @Override
    @Transactional
    public Mono<Void> update(String id, SettingDto dto) {
        return settingRepository.findById(id)
                .flatMap(setting -> updateFields(dto, setting))
                .then();
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
        return settingRepository.save(model);
    }

    @Override
    @Transactional
    public Mono<Void> delete(String id) {
        return settingRepository.deleteById(id);
    }

    @Override
    public Mono<SettingTypeDto> getType(String name) {
        return settingTypeRepository.findById(name).map(settingTypeMapper::toDto);
    }

    @Override
    public Flux<SettingTypeDto> getAllTypes() {
        return settingTypeRepository.findAll().map(settingTypeMapper::toDto);
    }
}

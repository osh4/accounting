package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import com.osh4.accounting.persistance.r2dbc.SettingType;
import com.osh4.accounting.persistance.repository.SettingRepository;
import com.osh4.accounting.persistance.repository.SettingTypeRepository;
import com.osh4.accounting.service.SettingService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private final Converter<Setting, SettingDto> settingConverter;
    private final Converter<SettingDto, Setting> settingReverseConverter;
    private final Converter<SettingType, SettingTypeDto> settingTypeConverter;

    @Override
    public Mono<Page<SettingDto>> getAll(PageRequest pageRequest) {
        return settingRepository.findAllBy(pageRequest)
                .map(settingConverter::convert)
                .collectList()
                .zipWith(settingRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    @Override
    public Mono<SettingDto> get(String id) {
        return settingRepository.findById(id)
                .map(settingConverter::convert)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    @Transactional
    public Mono<Setting> create(SettingDto dto) {
        return settingRepository.save(settingReverseConverter.convert(dto).setAsNew());
    }

    @Override
    @Transactional
    public Mono<Void> update(String id, SettingDto dto) {
        return settingRepository.findById(id)
                .flatMap(setting -> updateFields(dto, setting))
                .then();
    }

    private Mono<Setting> updateFields(SettingDto dto, Setting setting) {
        if (nonNull(dto) && isNotBlank(dto.getValue()) && ObjectUtils.notEqual(setting.getValue(), dto.getValue())) {
            setting.setValue(dto.getValue());
        }
        if (nonNull(dto) && isNotBlank(dto.getType()) && ObjectUtils.notEqual(setting.getType(), dto.getType())) {
            setting.setType(dto.getType());
        }
        return settingRepository.save(setting);
    }

    @Override
    @Transactional
    public Mono<Void> delete(String id) {
        return settingRepository.deleteById(id);
    }

    @Override
    public Mono<SettingTypeDto> getType(String name) {
        return settingTypeRepository.findById(name).map(settingTypeConverter::convert);
    }

    @Override
    public Flux<SettingTypeDto> getAllTypes() {
        return settingTypeRepository.findAll().map(settingTypeConverter::convert);
    }
}

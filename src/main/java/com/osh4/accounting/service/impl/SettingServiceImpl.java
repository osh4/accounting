package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import com.osh4.accounting.persistance.repository.SettingRepository;
import com.osh4.accounting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.osh4.accounting.utils.Constants.SETTINGS_TYPES;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
@Service
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;
    private final Converter<Setting, SettingDto> settingConverter;
    private final Converter<SettingDto, Mono<Setting>> settingReverseConverter;

    @Autowired
    public SettingServiceImpl(SettingRepository settingRepository,
                              Converter<Setting, SettingDto> settingConverter,
                              Converter<SettingDto, Mono<Setting>> settingReverseConverter) {
        this.settingRepository = settingRepository;
        this.settingConverter = settingConverter;
        this.settingReverseConverter = settingReverseConverter;
    }

    @Override
    public Flux<SettingDto> getAll() {
        return settingRepository.findAll().map(settingConverter::convert);
    }

    @Override
    public Mono<SettingDto> getByKey(String key) {
        return settingRepository.findByKey(key)
                .map(settingConverter::convert);
    }

    @Override
    public Mono<String> create(SettingDto settingDto) {
        return settingRepository.findByKey(settingDto.getKey())
                .flatMap(x -> Mono.error(new DuplicateKeyException("AlreadyExist")))
                .switchIfEmpty(settingReverseConverter.convert(settingDto).flatMap(settingRepository::save))
                .flatMap(x -> Mono.just("Add Success"));
    }

    @Override
    public Mono<String> update(SettingDto settingDto) {
        return settingRepository.findByKey(settingDto.getKey())
                .map(setting -> setValue(setting, settingDto))
                .flatMap(x -> Mono.just("Successful update!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with update")));
    }

    private Setting setValue(Setting settings, SettingDto dto) {
        settings.setValue(dto.getValue());
        settingRepository.save(settings).subscribe();
        return settings;
    }

    @Override
    public Mono<String> delete(SettingDto settingDto) {
        return settingRepository.findByKey(settingDto.getKey())
                .map(settingRepository::delete)
                .flatMap(x -> Mono.just("Successful remove!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with removal")));
    }

    @Override
    public Flux<String> getAllTypes() {
        return Flux.fromIterable(SETTINGS_TYPES);
    }
}

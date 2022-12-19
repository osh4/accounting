package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import com.osh4.accounting.persistance.repository.SettingsRepository;
import com.osh4.accounting.service.SettingsService;
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
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepository;
    private final Converter<Setting, SettingDto> settingsConverter;
    private final Converter<SettingDto, Mono<Setting>> settingsReverseConverter;

    @Autowired
    public SettingsServiceImpl(SettingsRepository settingsRepository,
                               Converter<Setting, SettingDto> settingsConverter,
                               Converter<SettingDto, Mono<Setting>> settingsReverseConverter) {
        this.settingsRepository = settingsRepository;
        this.settingsConverter = settingsConverter;
        this.settingsReverseConverter = settingsReverseConverter;
    }

    @Override
    public Flux<SettingDto> getAllSettings() {
        return settingsRepository.findAll().map(settingsConverter::convert);
    }

    @Override
    public Mono<String> create(SettingDto settingDto) {
        return settingsRepository.findByKey(settingDto.getKey())
                .flatMap(x -> Mono.error(new DuplicateKeyException("AlreadyExist")))
                .switchIfEmpty(settingsReverseConverter.convert(settingDto).flatMap(settingsRepository::save))
                .flatMap(x -> Mono.just("Add Success"));
    }

    @Override
    public Mono<String> update(SettingDto settingDto) {
        return settingsRepository.findByKey(settingDto.getKey())
                .map(setting -> setValue(setting, settingDto))
                .flatMap(x -> Mono.just("Successful update!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with update")));
    }

    private Setting setValue(Setting settings, SettingDto dto) {
        settings.setValue(dto.getValue());
        settingsRepository.save(settings).subscribe();
        return settings;
    }

    @Override
    public Mono<String> delete(SettingDto settingDto) {
        return settingsRepository.findByKey(settingDto.getKey())
                .map(settingsRepository::delete)
                .flatMap(x -> Mono.just("Successful remove!"))
                .switchIfEmpty(Mono.error(new Exception("Problems with removal")));
    }

    @Override
    public Flux<String> getAllTypes() {
        return Flux.fromIterable(SETTINGS_TYPES);
    }
}

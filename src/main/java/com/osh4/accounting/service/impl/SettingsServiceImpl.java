package com.osh4.accounting.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingsDto;
import com.osh4.accounting.persistance.entity.Settings;
import com.osh4.accounting.persistance.repository.SettingsRepository;
import com.osh4.accounting.service.SettingsService;

import static java.util.Objects.nonNull;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
@Service
public class SettingsServiceImpl implements SettingsService {
    private final SettingsRepository settingsRepository;
    private final Converter<Settings, SettingsDto> settingsConverter;
    private final Converter<SettingsDto, Settings> settingsReverseConverter;

    @Autowired
    public SettingsServiceImpl(SettingsRepository settingsRepository,
                               Converter<Settings, SettingsDto> settingsConverter,
                               Converter<SettingsDto, Settings> settingsReverseConverter) {
        this.settingsRepository = settingsRepository;
        this.settingsConverter = settingsConverter;
        this.settingsReverseConverter = settingsReverseConverter;
    }

    @Override
    public List<SettingsDto> getAllSettings() {
        final List<Settings> settings = settingsRepository.findAll();
        return settingsConverter.convertAll(settings);
    }

    @Override
    public void create(SettingsDto settingsDto) {
        Settings settings = settingsReverseConverter.convert(settingsDto);
        settingsRepository.save(settings);
    }

    @Override
    public SettingsDto update(SettingsDto settingsDto) {
        Settings setting = settingsRepository.findByGrpAndKey(settingsDto.getGroup(), settingsDto.getKey());
        if (nonNull(setting) && !Objects.equals(settingsDto.getValue(), setting.getValue())) {
            setting.setValue(settingsDto.getValue());
            settingsRepository.save(setting);
        }
        return settingsConverter.convert(setting);
    }

    @Override
    public void delete(SettingsDto settingsDto) {
        Settings setting = settingsRepository.findByGrpAndKey(settingsDto.getGroup(), settingsDto.getKey());

        if (nonNull(setting)) {
            settingsRepository.delete(setting);
        }
    }

    @Override
    public List<String> getAllTypes() {
        return List.of(String.class.getName(), Long.class.getName(), Boolean.class.getName());
    }
}

package com.osh4.accounting.service;

import java.util.List;

import com.osh4.accounting.dto.SettingsDto;
import com.osh4.accounting.persistance.entity.Settings;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
public interface SettingsService {
    List<SettingsDto> getAllSettings();

    void create(SettingsDto settingsDto);

    SettingsDto update(SettingsDto settingsDto);

    void delete(SettingsDto settingsDto);

    List<String> getAllTypes();
}

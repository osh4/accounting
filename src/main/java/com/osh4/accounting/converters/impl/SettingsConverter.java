package com.osh4.accounting.converters.impl;

import org.springframework.stereotype.Service;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingsDto;
import com.osh4.accounting.persistance.entity.Settings;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
@Service
public class SettingsConverter implements Converter<Settings, SettingsDto>
{
	@Override
	public SettingsDto convert(Settings settings)
	{
		SettingsDto dto = new SettingsDto();
		dto.setGroup(settings.getGrp());
		dto.setKey(settings.getKey());
		dto.setType(settings.getType());
		dto.setValue(settings.getValue());
		return dto;
	}
}

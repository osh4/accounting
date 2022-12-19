package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import org.springframework.stereotype.Service;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
@Service
public class SettingsConverter implements Converter<Setting, SettingDto>
{
	@Override
	public SettingDto convert(Setting settings)
	{
		SettingDto dto = new SettingDto();
		dto.setKey(settings.getKey());
		dto.setType(settings.getType());
		dto.setValue(settings.getValue());
		return dto;
	}
}

package com.osh4.accounting.converters.impl;

import org.springframework.stereotype.Service;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingsDto;
import com.osh4.accounting.persistance.entity.Settings;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
@Service
public class SettingsReverseConverter implements Converter<SettingsDto, Settings>
{
	@Override
	public Settings convert(SettingsDto settings)
	{
		Settings setting = new Settings();
		setting.setGrp(settings.getGroup());
		setting.setKey(settings.getKey());
		setting.setType(settings.getType());
		setting.setValue(settings.getValue());
		return setting;
	}
}

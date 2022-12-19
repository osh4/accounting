package com.osh4.accounting.converters.impl;

import com.osh4.accounting.persistance.r2dbc.Setting;
import org.springframework.stereotype.Service;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.SettingDto;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <kosntantin@osh4.com>
 */
@Service
public class SettingsReverseConverter implements Converter<SettingDto, Mono<Setting>>
{
	@Override
	public Mono<Setting> convert(SettingDto settings)
	{
		Setting setting = new Setting();
		setting.setKey(settings.getKey());
		setting.setType(settings.getType());
		setting.setValue(settings.getValue());
		return Mono.just(setting);
	}
}

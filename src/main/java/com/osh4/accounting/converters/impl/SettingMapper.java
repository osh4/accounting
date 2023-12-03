package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import org.mapstruct.Mapper;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring", uses = {SettingTypeMapper.class})
public interface SettingMapper {
    SettingDto toDto(Setting model);

    Setting toModel(SettingDto dto);

}

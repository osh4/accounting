package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring", uses = {SettingTypeMapper.class})
public interface SettingMapper {
    SettingDto toDto(Setting model);

    @Mapping(source = "settingType", target = "settingTypeId", qualifiedByName = "settingSaveMapper")
    Setting toModel(SettingDto dto);

    @Named("settingSaveMapper")
    static String settingSaveMapper(SettingTypeDto settingTypeDto) {
        return settingTypeDto.getId();
    }

}

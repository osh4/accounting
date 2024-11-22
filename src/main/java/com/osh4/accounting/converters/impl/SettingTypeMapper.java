package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.SettingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface SettingTypeMapper {
    SettingTypeDto toDto(SettingType model);

    @Mapping(target = "isNewEntity", ignore = true)
    SettingType toModel(SettingTypeDto dto);

}

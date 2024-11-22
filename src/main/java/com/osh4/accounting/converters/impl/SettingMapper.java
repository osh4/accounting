package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.SettingDto;
import com.osh4.accounting.dto.SettingTypeDto;
import com.osh4.accounting.persistance.r2dbc.Setting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface SettingMapper {
    @Mapping(source = "settingTypeId", target = "settingType", qualifiedByName = "settingTypeGetMapper")
    SettingDto toDto(Setting model);

    @Mapping(target = "isNewEntity", ignore = true)
    @Mapping(source = "settingType", target = "settingTypeId", qualifiedByName = "settingTypeSaveMapper")
    Setting toModel(SettingDto dto);

    @Named("settingTypeSaveMapper")
    static String settingTypeSaveMapper(SettingTypeDto dto) {
        return Optional.ofNullable(dto).map(SettingTypeDto::getId).orElse(null);
    }

    @Named("settingTypeGetMapper")
    static SettingTypeDto settingTypeGetMapper(String settingTypeId) {
        return Optional.ofNullable(settingTypeId)
                .map(typeId -> SettingTypeDto.builder().id(typeId).build())
                .orElse(null);
    }
}

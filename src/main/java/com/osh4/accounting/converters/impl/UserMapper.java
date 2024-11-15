package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.r2dbc.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;
import java.util.UUID;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User model);

    @Mapping(source = "id", target = "id", qualifiedByName = "userIdSaveMapper")
    User toModel(UserDto dto);

    @Named("userIdSaveMapper")
    static String userIdSaveMapper(String id) {
        return Optional.ofNullable(id).orElse(UUID.randomUUID().toString());
    }
}

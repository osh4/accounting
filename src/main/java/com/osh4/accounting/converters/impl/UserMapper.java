package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.r2dbc.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesGetMapper")
    UserDto toDto(User model);

    @Mapping(target = "isNewEntity", ignore = true)
    @Mapping(source = "id", target = "id", qualifiedByName = "userIdSaveMapper")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesSaveMapper")
    User toModel(UserDto dto);

    @Named("userIdSaveMapper")
    static String userIdSaveMapper(String id) {
        return Optional.ofNullable(id).orElse(UUID.randomUUID().toString());
    }

    @Named("rolesGetMapper")
    static Set<GrantedAuthority> rolesGetMapper(Set<String> roles) {
        if (isNull(roles)) {
            return null;
        }
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Named("rolesSaveMapper")
    static Set<String> rolesSaveMapper(Set<GrantedAuthority> authorities) {
        if (isNull(authorities)) {
            return null;
        }
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}

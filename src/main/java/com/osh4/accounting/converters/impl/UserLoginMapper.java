package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.dto.UserLoginResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface UserLoginMapper {
    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesGetMapper")
    UserLoginResponseDto toDto(UserDto model);

    @Named("rolesGetMapper")
    static Set<String> rolesGetMapper(Set<GrantedAuthority> roles) {
        if (isNull(roles)) {
            return null;
        }
        return roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}

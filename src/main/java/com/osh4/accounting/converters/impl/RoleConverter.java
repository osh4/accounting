package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.RoleDto;
import com.osh4.accounting.persistance.entity.user.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleConverter implements Converter<Role, RoleDto> {

    @Override
    public RoleDto convert(Role role) {
        RoleDto result = new RoleDto();
        result.setName(role.getName());
        return result;
    }
}

package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.RoleDto;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.dto.account.AccountDto;
import com.osh4.accounting.persistance.entity.account.Account;
import com.osh4.accounting.persistance.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class UserConverter implements Converter<User, UserDto> {
    private final RoleConverter roleConverter;

    @Autowired
    public UserConverter(RoleConverter roleConverter) {
        this.roleConverter = roleConverter;
    }

    @Override
    public UserDto convert(User user) {
        UserDto result = new UserDto();
        result.setLogin(user.getLogin());
        result.setName(user.getFirstName());
        result.setSurname(user.getLastName());
        result.setRoles(roleConverter.convertAll(user.getRoles()));
        result.setRolesString(getRolesString(result));
        return result;
    }

    private String getRolesString(UserDto result) {
        return result.getRoles().stream()
                .map(RoleDto::getName)
                .collect(Collectors.joining(", "));
    }
}

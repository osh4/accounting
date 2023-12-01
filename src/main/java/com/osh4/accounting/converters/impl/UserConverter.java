package com.osh4.accounting.converters.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.r2dbc.User;
import org.springframework.stereotype.Component;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
public class UserConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convertInternal(User model) {
        return UserDto.builder()
                .id(model.getId())
                .email(model.getEmail())
                .name(model.getName())
                .build();
    }
}

package com.osh4.accounting.converters.impl;

import com.osh4.accounting.dto.UserCredentialsDto;
import com.osh4.accounting.persistance.r2dbc.User;
import org.mapstruct.Mapper;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Mapper(componentModel = "spring")
public interface UserSignUpMapper {
    UserCredentialsDto toDto(User model);

    User toModel(UserCredentialsDto dto);
}

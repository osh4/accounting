package com.osh4.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String login;
    private String name;
    private String surname;
    private String password;
    private String passwordConfirm;
    private Set<RoleDto> roles;
    private String rolesString;
}

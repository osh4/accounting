package com.osh4.accounting.service;

import com.osh4.accounting.dto.RoleDto;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.entity.user.Role;
import com.osh4.accounting.persistance.entity.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserDto> getAllUsers();

    boolean saveUser(UserDto user);

    boolean deleteUser(String login);

    Role getRole(String name);

    List<Role> getAllRoles();

    boolean saveRole(RoleDto roleDto);
}

package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.UserConverter;
import com.osh4.accounting.dto.RoleDto;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.entity.user.Role;
import com.osh4.accounting.persistance.entity.user.User;
import com.osh4.accounting.persistance.repository.RoleRepository;
import com.osh4.accounting.persistance.repository.UserRepository;
import com.osh4.accounting.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.osh4.accounting.utils.Constants.Roles.ROLE_USER;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userConverter = userConverter;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);

        if (isNull(user)) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userConverter.convertAll(userRepository.findAll());
    }

    @Override
    public boolean saveUser(UserDto user) {
        if (isNull(user) || StringUtils.isBlank(user.getLogin())) {
            return false;
        }
        User userFromDB = userRepository.findByLogin(user.getLogin());

        if (nonNull(userFromDB)) {
            return false;
        }
        User newUser = new User();
        Role roleUser = getRole(ROLE_USER);
        if (isNull(roleUser)) {
            roleUser = new Role(ROLE_USER);
            roleRepository.save(roleUser);
        }
        newUser.setRoles(Collections.singleton(roleUser));
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
        return true;
    }

    @Override
    public boolean deleteUser(String login) {
        if (StringUtils.isBlank(login)) {
            return false;
        }
        User user = userRepository.findByLogin(login);
        if (nonNull(user)) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Override
    public Role getRole(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public boolean saveRole(RoleDto roleDto) {
        Role newRole = new Role();
        newRole.setName(roleDto.getName());
        roleRepository.save(newRole);
        return true;
    }
}

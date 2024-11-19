package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.UserMapper;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.r2dbc.User;
import com.osh4.accounting.persistance.repository.UserRepository;
import com.osh4.accounting.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<Page<UserDto>> getAll(PageRequest pageRequest) {
        return userRepository.findAllBy(pageRequest.withSort(Sort.by("id").descending()))
                .map(userMapper::toDto)
                .collectList()
                .zipWith(userRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    @Override
    public Mono<UserDto> get(String id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<UserDto> create(UserDto dto) {
        return Mono.just(dto)
                .map(userMapper::toModel)
                .map(User::setAsNew)
                .map(this::encodePassword)
                .flatMap(userRepository::save)
                .map(userMapper::toDto);
    }

    private User encodePassword(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return u;
    }

    @Override
    public Mono<UserDto> update(String id, UserDto dto) {
        return userRepository.findById(id)
                .flatMap(model -> updateFields(model, dto))
                .map(userMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return userRepository.deleteById(id);
    }

    private Mono<User> updateFields(User model, UserDto dto) {
        if (isNull(dto)) {
            return Mono.just(model);
        }
        if (isNotBlank(dto.getName()) && ObjectUtils.notEqual(model.getName(), dto.getName())) {
            model.setName(dto.getName());
        }
        if (isNotBlank(dto.getEmail()) && ObjectUtils.notEqual(model.getEmail(), dto.getEmail())) {
            model.setEmail(dto.getEmail());
        }
        if (isNotBlank(dto.getPassword()) && !passwordEncoder.matches(dto.getPassword(), model.getPassword())) {
            model.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (ObjectUtils.notEqual(model.isEnabled(), dto.isEnabled())) {
            model.setEnabled(dto.isEnabled());
        }
        if (CollectionUtils.isNotEmpty(dto.getRoles()) && ObjectUtils.notEqual(model.getRoles(), dto.getRoles())) {
            model.setRoles(dto.getRoles().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        }
        return userRepository.save(model);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorResume(it -> Mono.empty())
                .map(userMapper::toDto)
                .map(UserDetails.class::cast)
                .switchIfEmpty(Mono.empty())
                .doOnTerminate(() -> log.warn("User for id {} not found", username));
    }
}

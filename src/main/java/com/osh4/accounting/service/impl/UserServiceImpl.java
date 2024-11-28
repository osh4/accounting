package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.impl.UserMapper;
import com.osh4.accounting.converters.impl.UserSignUpMapper;
import com.osh4.accounting.dto.UserCredentialsDto;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.exception.AlreadyExistsException;
import com.osh4.accounting.exception.NotFoundException;
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
    private UserRepository repository;
    private UserMapper mapper;
    private UserSignUpMapper signUpMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<Page<UserDto>> getAll(PageRequest pageRequest) {
        return repository.findAllBy(pageRequest.withSort(Sort.by("id").descending()))
                .map(mapper::toDto)
                .collectList()
                .zipWith(repository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    @Override
    public Mono<UserDto> get(String id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(NotFoundException.fromUserEmail(id)));
    }

    @Override
    public Mono<UserDto> create(UserDto dto) {
        return repository.findByEmail(dto.getEmail())
                .switchIfEmpty(Mono.just(mapper.toModel(dto).setAsNew()).map(this::encodePassword).flatMap(repository::save))
                .filter(User::isNewEntity)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(AlreadyExistsException.fromUserEmail(dto.getEmail())));
    }

    @Override
    public Mono<UserDto> signUp(UserCredentialsDto dto) {
        return repository.findByEmail(dto.getEmail())
                .switchIfEmpty(Mono.just(signUpMapper.toModel(dto).setAsNew()).map(this::encodePassword).flatMap(repository::save))
                .filter(User::isNewEntity)
                .map(mapper::toDto)
                .switchIfEmpty(Mono.error(AlreadyExistsException.fromUserEmail(dto.getEmail())));
    }

    private User encodePassword(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return u;
    }

    @Override
    public Mono<UserDto> update(String email, UserDto dto) {
        return repository.findByEmail(email)
                .switchIfEmpty(Mono.error(NotFoundException.fromUserEmail(email)))
                .flatMap(model -> updateFields(model, dto))
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> delete(String email) {
        return repository.findByEmail(email)
                .switchIfEmpty(Mono.error(NotFoundException.fromUserEmail(email)))
                .flatMap(user -> repository.deleteById(user.getId()));
    }

    private Mono<User> updateFields(User model, UserDto dto) {
        if (isNull(dto) || isNull(model)) {
            return Mono.justOrEmpty(model);
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
        return repository.save(model);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return repository.findByEmail(username)
                .switchIfEmpty(Mono.error(NotFoundException.fromUserEmail(username)))
                .doOnError(error -> log.error(error.getMessage(), error))
                .map(mapper::toDto);
    }
}

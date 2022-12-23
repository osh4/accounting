package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.r2dbc.User;
import com.osh4.accounting.persistance.repository.UserRepository;
import com.osh4.accounting.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private Converter<User, UserDto> userConverter;
    private Converter<UserDto, User> userReverseConverter;

    @Override
    public Flux<UserDto> getAll() {
        return userRepository.findAll()
                .map(userConverter::convert);
    }

    @Override
    public Mono<UserDto> get(String id) {
        return userRepository.findById(id)
                .map(userConverter::convert)
                .switchIfEmpty(Mono.error(new Exception()));
    }

    @Override
    public Mono<User> create(UserDto dto) {
        return userRepository.save(userReverseConverter.convert(dto).setAsNew());
    }

    @Override
    public Mono<Void> update(UserDto dto) {
        return userRepository.findById(dto.getId())
                .flatMap(model -> updateFields(model, dto))
                .then();
    }

    @Override
    public Mono<Void> delete(UserDto dto) {
        return userRepository.deleteById(dto.getId());
    }

    private Mono<User> updateFields(User model, UserDto dto) {
        if (nonNull(dto.getName()) && ObjectUtils.notEqual(model.getName(), dto.getName())) {
            model.setName(dto.getName());
        }
        if (nonNull(dto.getEmail()) && ObjectUtils.notEqual(model.getEmail(), dto.getEmail())) {
            model.setEmail(dto.getEmail());
        }
        return userRepository.save(model);
    }
}

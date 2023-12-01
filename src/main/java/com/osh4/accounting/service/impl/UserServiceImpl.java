package com.osh4.accounting.service.impl;

import com.osh4.accounting.converters.Converter;
import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.r2dbc.User;
import com.osh4.accounting.persistance.repository.UserRepository;
import com.osh4.accounting.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private Converter<User, UserDto> userConverter;
    private Converter<UserDto, User> userReverseConverter;

    @Override
    public Mono<Page<UserDto>> getAll(PageRequest pageRequest) {
        return userRepository.findAllBy(pageRequest.withSort(Sort.by("id").descending()))
                .map(userConverter::convert)
                .collectList()
                .zipWith(userRepository.count())
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
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
    public Mono<Void> update(String id, UserDto dto) {
        return userRepository.findById(id)
                .flatMap(model -> updateFields(model, dto))
                .then();
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
        return userRepository.save(model);
    }
}

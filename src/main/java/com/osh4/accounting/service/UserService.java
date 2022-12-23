package com.osh4.accounting.service;

import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.r2dbc.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface UserService {

    Flux<UserDto> getAll();

    Mono<UserDto> get(String id);

    Mono<User> create(UserDto dto);

    Mono<Void> update(UserDto dto);

    Mono<Void> delete(UserDto dto);
}

package com.osh4.accounting.service;

import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.persistance.r2dbc.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface UserService {

    Mono<Page<UserDto>> getAll(PageRequest pageRequest);

    Mono<UserDto> get(String id);

    Mono<User> create(UserDto dto);

    Mono<Void> update(String id, UserDto dto);

    Mono<Void> delete(String id);
}

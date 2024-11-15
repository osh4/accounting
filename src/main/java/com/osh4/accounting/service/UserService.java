package com.osh4.accounting.service;

import com.osh4.accounting.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface UserService {

    Mono<Page<UserDto>> getAll(PageRequest pageRequest);

    Mono<UserDto> get(String id);

    Mono<UserDto> create(UserDto dto);

    Mono<UserDto> update(String id, UserDto dto);

    Mono<Void> delete(String id);
}

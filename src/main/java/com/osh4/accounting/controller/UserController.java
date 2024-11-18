package com.osh4.accounting.controller;

import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @GetMapping("/all")
    public Mono<Page<UserDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id_asc") String sort) {
        return userService.getAll(paginatedSearchService.paginationInfo(page, size, sort));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> update(@PathVariable String id, @RequestBody UserDto dto) {
        return userService.update(id, dto)
                .flatMap(this::successResponse)
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponse());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
        return userService.delete(id)
                .flatMap(s -> successResponseDelete())
                .doOnError(error -> log.error(error.getMessage(), error))
                .onErrorReturn(failResponseDelete());
    }

}

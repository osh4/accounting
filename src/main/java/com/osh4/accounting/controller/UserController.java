package com.osh4.accounting.controller;

import com.osh4.accounting.dto.UserDto;
import com.osh4.accounting.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{email}")
    public Mono<ResponseEntity<UserDto>> update(@PathVariable String email, @RequestBody UserDto dto) {
        return userService.update(email, dto)
                .flatMap(this::successResponse);
    }

    @DeleteMapping("/{email}")
    public Mono<ResponseEntity<String>> delete(@PathVariable String email) {
        return userService.delete(email)
                .then(Mono.defer(this::successResponseDelete));
    }

}

package com.osh4.accounting.controller;

import com.osh4.accounting.service.PaginatedSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static com.osh4.accounting.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public class BaseController {

    @Autowired
    protected PaginatedSearchService paginatedSearchService;

    protected <T> Mono<ResponseEntity<T>> successResponse(T body) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body));
    }

    protected String getEntityName() {
        return this.getClass().getSimpleName().replace("Controller", StringUtils.EMPTY);
    }

    protected Mono<ResponseEntity<String>> successResponseDelete() {
        return successResponse(MSG_THE + SPACE + getEntityName() + SPACE + MSG_DELETE_SUCCESS);
    }
}

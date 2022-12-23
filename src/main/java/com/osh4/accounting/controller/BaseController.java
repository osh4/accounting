package com.osh4.accounting.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static com.osh4.accounting.utils.Constants.*;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public class BaseController {
    protected <T> Mono<ResponseEntity<T>> successResponse(T body) {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(body));
    }

    protected <T> ResponseEntity<T> failResponse(T body) {
        return ResponseEntity.unprocessableEntity().body(body);
    }
    protected <T> ResponseEntity<T> failResponse() {
        return ResponseEntity.unprocessableEntity().build();
    }

    protected String getEntityName() {
        return this.getClass().getName().replace("Controller", StringUtils.EMPTY);
    }

    protected Mono<ResponseEntity<String>> successResponseCreate() {
        return successResponse(MSG_THE + getEntityName() + StringUtils.SPACE + MSG_CREATE_SUCCESS);
    }

    protected Mono<ResponseEntity<String>> successResponseUpdate() {
        return successResponse(MSG_THE + getEntityName() + StringUtils.SPACE + MSG_UPDATE_SUCCESS);
    }

    protected Mono<ResponseEntity<String>> successResponseDelete() {
        return successResponse(MSG_THE + getEntityName() + StringUtils.SPACE + MSG_DELETE_SUCCESS);
    }

    protected ResponseEntity<String> failResponseCreate() {
        return failResponse(MSG_CREATE_FAIL + MSG_THE + getEntityName());
    }

    protected ResponseEntity<String> failResponseUpdate() {
        return failResponse(MSG_UPDATE_FAIL + MSG_THE + getEntityName());
    }

    protected ResponseEntity<String> failResponseDelete() {
        return failResponse(MSG_DELETE_FAIL + MSG_THE + getEntityName());
    }
}

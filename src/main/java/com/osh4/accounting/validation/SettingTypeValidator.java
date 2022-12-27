package com.osh4.accounting.validation;

import com.osh4.accounting.service.SettingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
@AllArgsConstructor
public class SettingTypeValidator implements Validator<String> {
    private SettingService service;

    @Override
    public boolean validate(String value) {
        return Boolean.TRUE.equals(service.getType(value)
                .map(x -> true)
                .switchIfEmpty(Mono.just(false))
                .block());
    }
}

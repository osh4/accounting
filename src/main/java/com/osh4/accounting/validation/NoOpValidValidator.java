package com.osh4.accounting.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
@Slf4j
public class NoOpValidValidator implements Validator<String> {
    @Override
    public boolean validate(String value) {
        log.info("This validator do nothing and returns true for any value");
        return true;
    }
}

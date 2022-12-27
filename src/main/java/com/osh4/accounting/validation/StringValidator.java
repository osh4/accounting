package com.osh4.accounting.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author osh4 <konstantin@osh4.com>
 */
@Component
public class StringValidator implements Validator<String> {

    private static final int MAX_SYMBOLS = 255;

    @Override
    public boolean validate(String value) {
        return StringUtils.isNotBlank(value) && value.length() < MAX_SYMBOLS;
    }
}

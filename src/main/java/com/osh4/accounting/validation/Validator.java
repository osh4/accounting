package com.osh4.accounting.validation;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public interface Validator<T> {
    boolean validate(T value);
}

package com.osh4.accounting.exception;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException fromCurrencyIsoCode(String isoCode) {
        return new NotFoundException(String.format("Currency with isocode %s not found", isoCode));
    }

    public static NotFoundException fromUserEmail(String email) {
        return new NotFoundException(String.format("User with email %s not found", email));
    }
}

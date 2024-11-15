package com.osh4.accounting.exception;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public class CannotPerformTransaction extends RuntimeException {
    public CannotPerformTransaction(String message) {
        super(message);
    }
}

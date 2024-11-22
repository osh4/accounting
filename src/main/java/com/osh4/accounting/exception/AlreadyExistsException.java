package com.osh4.accounting.exception;

/**
 * @author osh4 <konstantin@osh4.com>
 */
public class AlreadyExistsException extends Exception {
    public AlreadyExistsException(String message) {
        super(message);
    }

    public static AlreadyExistsException fromCurrencyIsoCode(String isoCode) {
        return new AlreadyExistsException(String.format("Currency with isocode [%s] already exist", isoCode));
    }

    public static AlreadyExistsException fromUserEmail(String email) {
        return new AlreadyExistsException(String.format("User with email [%s] already exist", email));
    }

    public static AlreadyExistsException fromAccountName(String accountName) {
        return new AlreadyExistsException(String.format("Account with name %s already exist", accountName));
    }

    public static AlreadyExistsException fromTransactionTypeName(String transactionTypeName) {
        return new AlreadyExistsException(String.format("Transaction type with name %s already exist", transactionTypeName));
    }

    public static AlreadyExistsException fromTransactionCategoryId(String transactionCategoryId) {
        return new AlreadyExistsException(String.format("Transaction category with id %s already exist", transactionCategoryId));
    }

    public static AlreadyExistsException fromTransactionId(String transactionId) {
        return new AlreadyExistsException(String.format("Transaction with id %s already exist", transactionId));
    }

    public static AlreadyExistsException fromSettingId(String settingId) {
        return new AlreadyExistsException(String.format("Setting with name %s already exist", settingId));
    }
}

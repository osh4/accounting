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

    public static NotFoundException fromAccountId(String accountId) {
        return new NotFoundException(String.format("Account with id %s not found", accountId));
    }

    public static NotFoundException fromTransactionTypeId(String transactionTypeId) {
        return new NotFoundException(String.format("Transaction type with id %s not found", transactionTypeId));
    }

    public static NotFoundException fromTransactionCategoryId(String transactionCategoryId) {
        return new NotFoundException(String.format("Transaction category with id %s not found", transactionCategoryId));
    }

    public static NotFoundException fromTransactionId(String transactionId) {
        return new NotFoundException(String.format("Transaction with id %s not found", transactionId));
    }

    public static AlreadyExistsException fromSettingId(String settingId) {
        return new AlreadyExistsException(String.format("Setting with id %s not found", settingId));
    }

    public static AlreadyExistsException fromSettingTypeName(String settingTypeName) {
        return new AlreadyExistsException(String.format("Setting type with name %s not found", settingTypeName));
    }
}

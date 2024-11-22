package com.osh4.accounting.utils;

import java.util.List;
import java.util.Locale;

public class Constants {

    public static final String MSG_DELETE_SUCCESS = "was deleted successfully";
    public static final String MSG_DELETE_FAIL = "Can't delete";
    public static final String MSG_THE = "The";

    public static Locale ru_RU = new Locale("ru", "RU");
    public static Locale ru = new Locale("ru", "");

    public static final class Settings {
        public static final String DEFAULT_TRANSACTION_CATEGORY_KEY = "default.transaction.category";
        public static final String DEFAULT_TARGET_ACCOUNT_KEY = "default.target.account";
        public static final String DEFAULT_TRANSACTION_TYPE_KEY = "default.transaction.type";
    }

    public static final class Roles {
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
    }

    public static final List<String> SETTINGS_TYPES = List.of(String.class.getName(),
            Long.class.getName(), Boolean.class.getName());
}

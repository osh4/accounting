package com.osh4.accounting.utils;

import java.util.List;
import java.util.Locale;

public class Constants {
    public static Locale ru_RU = new Locale("ru", "RU");
    public static Locale ru = new Locale("ru", "");

    public static final class Roles {
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
    public static final List<String> SETTINGS_TYPES = List.of(String.class.getName(),
            Long.class.getName(), Boolean.class.getName());
}

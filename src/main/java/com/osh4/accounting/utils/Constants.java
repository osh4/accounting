package com.osh4.accounting.utils;

import java.util.List;
import java.util.Locale;

public class Constants {

    public static final String MSG_DELETE_SUCCESS = "Successfully";
    public static final String MSG_UPDATE_SUCCESS = "was successfully updated!";
    public static final String MSG_CREATE_SUCCESS = "was successfully created!";
    public static final String MSG_UPDATE_FAIL = "Can't update";
    public static final String MSG_CREATE_FAIL = "Can't create";
    public static final String MSG_DELETE_FAIL = "Can't delete";
    public static final String MSG_THE = "The";

    public static Locale ru_RU = new Locale("ru", "RU");
    public static Locale ru = new Locale("ru", "");

    public static final class Roles {
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
    }

    public static final List<String> SETTINGS_TYPES = List.of(String.class.getName(),
            Long.class.getName(), Boolean.class.getName());
}

package com.infinibet.config;

import java.math.BigDecimal;

/**
 * Application constants.
 */
public final class AppConstants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String DEFAULT_LANGUAGE = "en";

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 0;

    public static final BigDecimal START_VIRTUAL_BALANCE = new BigDecimal(1000);

    private AppConstants() {
    }
}

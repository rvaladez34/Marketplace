package com.intuit.cg.backendtechassessment.model.validator;

import java.math.BigDecimal;

public class ValidationRegex {
    public static final String SAFE_TEXT_REGEX = "^[a-zA-Z0-9 .-]{15,200}$";
    public static final String SAFE_NAME_REGEX = "^[a-zA-Z0-9 .-]+$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final int DESC_MINIMUM_CHAR_COUNT = 15;
    public static final int DESC_MAX_CHAR_COUNT = 200;
}

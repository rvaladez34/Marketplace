package com.intuit.cg.backendtechassessment.model.validator;

public class ErrorMessages {

    public static final String PROJECT_ID_NOT_FOUND = "Project ID not found";
    public static final String MISSING_DESCRIPTION = "Missing description";
    public static final String DOES_NOT_CONTAIN_THE_MINIMUM_CHARACTER_COUNT = "Does not contain the minimum {} character count";
    public static final String MAXIMUM_CHARACTER_COUNT_REACHED = "Maximum {} character count reached";
    public static final String INVALID_CHARACTERS_USED_IN_DESCRIPTION = "Invalid characters used in description";
    public static final String MISSING_CLOSING_DATE = "Missing closing date";
    public static final String INVALID_DATE_FORMAT = "Invalid date format";
    public static final String MISSING_PERSON_S_NAME = "Missing person's name";
    public static final String INVALID_PERSON_S_NAME = "Invalid person's name";
    public static final String MISSING_PERSON_S_EMAIL = "Missing person's email";
    public static final String INVALID_PERSON_S_EMAIL = "Invalid person's email";
    public static final String INVALID_BID_DATE = "Bid is added after project closing date";
    public static final String ERROR_GETTING_PROJECT_FORMAT = "Error getting project closing date";


    public static final String BID_IS_GREATER_THAN_MAX_BUDGET_AMOUNT = "Bid is greater than max budget amount";
    public static final String BID_MUST_BE_LOWER_THAN_THE_CURRENT_LOWEST_BID = "Bid must be at least {} lower than the current lowest bid";
    public static final String YOUR_BID_WAS_OUT_BID = "Your bid was out bid";
    public static final String MISSING_NUMBER_FOR_FOR_PRICE = "Missing number for {} price";
    public static final String INVALID_NUMBER_FORMAT_FOR_PRICE = "Invalid number format for {} price";
}

package com.intuit.cg.backendtechassessment.model.validator;

import com.intuit.cg.backendtechassessment.manager.BidManager;
import com.intuit.cg.backendtechassessment.model.Person;
import com.intuit.cg.backendtechassessment.model.Price;
import com.intuit.cg.backendtechassessment.model.Project;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

import static com.intuit.cg.backendtechassessment.model.validator.ValidationRegex.*;

public class Validator {

    private static void logError(String message) {
        LogFactory.getLog(Validator.class).info(message);
    }

    public static List<String> validProject(Project project) {
        List<String> messages = new ArrayList<>();

        messages.addAll(validDescription(project.getDescription()));
        messages.addAll(validDate(project.getClosingDate()));
        messages.addAll(validPrice(project.getMaxBudget(), "maxBudget"));
        messages.addAll(validPerson(project.getSeller()));

        if (!messages.isEmpty()) {
            logError("Project object was invalid messages=" + messages);
        }
        return messages;
    }

    private static List<String> validDescription(String description) {
        List<String> messages = new ArrayList<>();

        if (StringUtils.isEmpty(description)) messages.add(ErrorMessages.MISSING_DESCRIPTION);
        else if (description.length() < DESC_MINIMUM_CHAR_COUNT) messages.add(ErrorMessages.DOES_NOT_CONTAIN_THE_MINIMUM_CHARACTER_COUNT.replace("{}", Integer.toString(DESC_MINIMUM_CHAR_COUNT)));
        else if (description.length() > DESC_MAX_CHAR_COUNT) messages.add(ErrorMessages.MAXIMUM_CHARACTER_COUNT_REACHED.replace("{}", Integer.toString(DESC_MAX_CHAR_COUNT)));
        else if (!matches(description, SAFE_TEXT_REGEX)) messages.add(ErrorMessages.INVALID_CHARACTERS_USED_IN_DESCRIPTION);

        return messages;
    }

    public static List<String> validPrice(Price price, String label) {
        List<String> messages = new ArrayList<>();
        if (null == price || null == price.getValue()) {
            messages.add(ErrorMessages.MISSING_NUMBER_FOR_FOR_PRICE.replace("{}", label));
        } else if (price.getValue().compareTo(BigDecimal.ZERO) < 0) {
            messages.add(ErrorMessages.INVALID_NUMBER_FORMAT_FOR_PRICE.replace("{}", label));
        }
        return messages;
    }

    private static List<String> validDate(String closingDate) {
        List<String> messages = new ArrayList<>();

        if (StringUtils.isEmpty(closingDate)) {
            messages.add(ErrorMessages.MISSING_CLOSING_DATE);
        } else {
            try {
                LocalDate date = LocalDate.parse(closingDate);
                if (date == null) {
                    messages.add(ErrorMessages.INVALID_DATE_FORMAT);
                }
            } catch (DateTimeParseException e) {
                messages.add(ErrorMessages.INVALID_DATE_FORMAT);
            }
        }

        return messages;
    }

    public static List<String> validPerson(Person p) {
        List<String> messages = new ArrayList<>();

        if (StringUtils.isEmpty(p.getName())) {
            messages.add(ErrorMessages.MISSING_PERSON_S_NAME);
        } else if (!matches(p.getName(), (SAFE_NAME_REGEX))) {
            messages.add(ErrorMessages.INVALID_PERSON_S_NAME);
        }

        if (StringUtils.isEmpty(p.getEmail())) {
            messages.add(ErrorMessages.MISSING_PERSON_S_EMAIL);
        } else if (!matches(p.getEmail(), EMAIL_REGEX)) {
            messages.add(ErrorMessages.INVALID_PERSON_S_EMAIL);
        }

        return messages;
    }

    private static boolean matches(String input, String regex) {
        return Pattern.compile(regex).matcher(input).matches();
    }

    /**
     * Compares an amount against a project's current lowest bid
     * <p>
     * 1. compares the price is lower than the budget
     * 2. compares the price is lower than the current fixed bid
     * 3. compares the price is lower than the current auto bid
     *
     * @param price   required
     * @param project required
     * @return list of errors if price is not lower than current bid
     */
    public static List<String> compareWithLowestBid(Price price, Project project) {
        List<String> messages = new ArrayList<>();
        if (null != project.getLowestBid()
                && !Validator.isLowerByBidIncrement(price.getValue(), project.getLowestBid().getAmount().getValue())) {
            messages.add(ErrorMessages.BID_MUST_BE_LOWER_THAN_THE_CURRENT_LOWEST_BID.replace("{}", BidManager.formattedBidDecrement()));
        }
        //check current auto bid
        if (messages.isEmpty()
                && null != project.getLowestBid().getAutoBid()) {
            if (project.getLowestBid().getAutoBid().getValue().compareTo(price.getValue()) <= 0) {
                messages.add(ErrorMessages.YOUR_BID_WAS_OUT_BID); //need to update project.lowestBid.amount
            }
        }
        return messages;
    }

    private static boolean isLowerByBidIncrement(BigDecimal newBid, BigDecimal currentBid) {
        return currentBid.subtract(newBid).compareTo(BidManager.getAutoDecrementAmount()) >= 0;
    }

    public static List<String> validPriceAgainstBudget(Price price, Project project) {
        List<String> messages = new ArrayList<>();
        if (price.getValue().compareTo(project.getMaxBudget().getValue()) > 0) {
            messages.add(ErrorMessages.BID_IS_GREATER_THAN_MAX_BUDGET_AMOUNT);
        }
        return messages;
    }


    /**
     * Validates closingDate with LocalDate.class
     * @param closingDate string
     * @return list of any errors
     */
    public static List<String> validBidDate(String closingDate) {
        boolean isInternalError = false;
        List<String> messages = new ArrayList<>();
        try {
            LocalDate date = LocalDate.parse(closingDate);
            if (date == null) {
                isInternalError = true;
            } else if (LocalDate.now().isAfter(date)) {
                messages.add(ErrorMessages.INVALID_BID_DATE);
            }
        } catch (Exception e) {
            isInternalError = true;
        }

        if (isInternalError) throw new DateTimeParseException(ErrorMessages.ERROR_GETTING_PROJECT_FORMAT, closingDate, 0);

        return messages;
    }
}

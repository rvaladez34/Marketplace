package com.intuit.cg.backendtechassessment.model.validator;

import com.intuit.cg.backendtechassessment.BackendTechAssessmentApplicationTests;
import com.intuit.cg.backendtechassessment.manager.BidManager;
import com.intuit.cg.backendtechassessment.model.Bid;
import com.intuit.cg.backendtechassessment.model.Person;
import com.intuit.cg.backendtechassessment.model.Price;
import com.intuit.cg.backendtechassessment.model.Project;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static com.intuit.cg.backendtechassessment.model.validator.ErrorMessages.*;
import static org.junit.Assert.*;

public class ValidatorTest {

    @Before
    public void setUp() throws Exception {
        new BidManager(null,10);
    }

    @Test
    public void validProject() throws Exception {
        Project p = BackendTechAssessmentApplicationTests.mockProject();
        List<String> errors = Validator.validProject(p);
        assertThat(errors, empty());
    }

    @Test
    public void missingValidProject() throws Exception {
        Project p = new Project()
                .setDescription("")
                .setMaxBudget(new Price()
                        .setValue(null))
                .setClosingDate("")
                .setSeller(new Person()
                        .setName("")
                        .setEmail(null));

        List<String> errors = Validator.validProject(p);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(MISSING_DESCRIPTION));
        assertThat(errors, hasItem(MISSING_CLOSING_DATE));
        assertThat(errors, hasItem(MISSING_NUMBER_FOR_FOR_PRICE.replace("{}", "maxBudget")));
        assertThat(errors, hasItem(MISSING_PERSON_S_NAME));
        assertThat(errors, hasItem(MISSING_PERSON_S_EMAIL));
    }

    @Test
    public void invalidValidProject() throws Exception {
        Project p = new Project()
                .setDescription("too short")
                .setMaxBudget(new Price()
                        .setValue(new BigDecimal(-20)))
                .setClosingDate("Not a date")
                .setSeller(new Person()
                        .setName("b@d ^@me")
                        .setEmail("not @n email"));

        List<String> errors = Validator.validProject(p);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(DOES_NOT_CONTAIN_THE_MINIMUM_CHARACTER_COUNT.replace("{}", Integer.toString(ValidationRegex.DESC_MINIMUM_CHAR_COUNT))));
        assertThat(errors, hasItem(INVALID_NUMBER_FORMAT_FOR_PRICE.replace("{}", "maxBudget")));
        assertThat(errors, hasItem(INVALID_DATE_FORMAT));
        assertThat(errors, hasItem(INVALID_PERSON_S_NAME));
        assertThat(errors, hasItem(INVALID_PERSON_S_EMAIL));
    }

    @Test
    public void invalid2ValidProject() throws Exception {
        Project p = new Project()
                .setDescription("too long Lorem ipsum dolor sit amet, ex vero tation noster mea, ex veri melius sed, odio altera vituperata pri ei. Eu tritani principes vim, eu veniam nonumy aperiri nam. An saepe option deterruisset sed. Scripta delicata eam te, vim te erat oratio blandit. Ocurreret suscipiantur pro ea, ea sit laudem discere.")
                .setMaxBudget(new Price()
                        .setValue(new BigDecimal(-20)))
                .setClosingDate("Not a date")
                .setSeller(new Person()
                        .setName("b@d ^@me")
                        .setEmail("not @n email"));

        List<String> errors = Validator.validProject(p);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(MAXIMUM_CHARACTER_COUNT_REACHED.replace("{}", Integer.toString(ValidationRegex.DESC_MAX_CHAR_COUNT))));
        assertThat(errors, hasItem(INVALID_NUMBER_FORMAT_FOR_PRICE.replace("{}", "maxBudget")));
        assertThat(errors, hasItem(INVALID_DATE_FORMAT));
        assertThat(errors, hasItem(INVALID_PERSON_S_NAME));
        assertThat(errors, hasItem(INVALID_PERSON_S_EMAIL));
    }

    @Test
    public void invalid3ValidProject() throws Exception {
        Project p = new Project()
                .setDescription("invalid desc @#$%^&*()")
                .setMaxBudget(new Price()
                        .setValue(new BigDecimal(-20)))
                .setClosingDate("Not a date")
                .setSeller(new Person()
                        .setName("b@d ^@me")
                        .setEmail("not @n email"));

        List<String> errors = Validator.validProject(p);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(INVALID_CHARACTERS_USED_IN_DESCRIPTION));
        assertThat(errors, hasItem(INVALID_NUMBER_FORMAT_FOR_PRICE.replace("{}", "maxBudget")));
        assertThat(errors, hasItem(INVALID_DATE_FORMAT));
        assertThat(errors, hasItem(INVALID_PERSON_S_NAME));
        assertThat(errors, hasItem(INVALID_PERSON_S_EMAIL));
    }

    @Test
    public void validatePrice() throws Exception {
        Price validPrice = new Price().setValue(new BigDecimal(100.00));
        List<String> errors = Validator.validPrice(validPrice, "test");
        assertTrue(CollectionUtils.isEmpty(errors));
    }

    @Test
    public void negativeValidatePrice() throws Exception {
        String label = "test";
        Price validPrice = new Price().setValue(new BigDecimal(-10.00));
        List<String> errors = Validator.validPrice(validPrice, label);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(INVALID_NUMBER_FORMAT_FOR_PRICE.replace("{}", label)));
    }

    @Test
    public void missing1ValidatePrice() throws Exception {
        String label = "test";
        Price validPrice = null;
        List<String> errors = Validator.validPrice(validPrice, label);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(MISSING_NUMBER_FOR_FOR_PRICE.replace("{}", label)));
    }
    @Test
    public void missing2ValidatePrice() throws Exception {
        String label = "test";
        Price validPrice = new Price();
        List<String> errors = Validator.validPrice(validPrice, label);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(MISSING_NUMBER_FOR_FOR_PRICE.replace("{}", label)));
    }

    @Test
    public void validatePerson() throws Exception {
        Person p = new Person()
                .setName("test")
                .setEmail("test@test.com");

        List<String> errors = Validator.validPerson(p);
        assertTrue(CollectionUtils.isEmpty(errors));
    }

    @Test
    public void missingValidatePerson() throws Exception {
        Person p = new Person()
                .setName("")
                .setEmail("");

        List<String> errors = Validator.validPerson(p);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(MISSING_PERSON_S_NAME));
        assertThat(errors, hasItem(MISSING_PERSON_S_EMAIL));
    }

    @Test
    public void invalidValidatePerson() throws Exception {
        Person p = new Person()
                .setName("*&*&(^")
                .setEmail("asifh*&^a209@38ssasdfff");

        List<String> errors = Validator.validPerson(p);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(INVALID_PERSON_S_NAME));
        assertThat(errors, hasItem(INVALID_PERSON_S_EMAIL));
    }

    @Test
    public void compareWithLowestBid() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject()
                .setLowestBid(new Bid().setAmount(new Price().setValue(new BigDecimal(150.00))));

        Price p = new Price().setValue(new BigDecimal(100.00));
        List<String> errors = Validator.compareWithLowestBid(p, project);
        assertThat(errors, empty());
    }

    @Test
    public void AutoBidCompareWithLowestBid() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject()
                .setLowestBid(new Bid()
                        .setAmount(new Price().setValue(new BigDecimal(150.00)))
                        .setAutoBid(new Price().setValue(new BigDecimal(110.00))));

        Price p = new Price().setValue(new BigDecimal(100.00));
        List<String> errors = Validator.compareWithLowestBid(p, project);
        assertThat(errors, empty());
    }

    @Test
    public void invalidCompareWithLowestBid() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject()
                .setLowestBid(new Bid().setAmount(new Price().setValue(new BigDecimal(150.00))));

        Price p = new Price().setValue(new BigDecimal(145.00));
        List<String> errors = Validator.compareWithLowestBid(p, project);
        assertThat(errors, not(empty()));
        String error = BID_MUST_BE_LOWER_THAN_THE_CURRENT_LOWEST_BID
                .replace("{}", BidManager.formattedBidDecrement());
        assertThat(errors, hasItem(error));
    }

    @Test
    public void AutoBidOutBidCompareWithLowestBid() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject()
                .setLowestBid(new Bid()
                        .setAmount(new Price().setValue(new BigDecimal(150.00)))
                        .setAutoBid(new Price().setValue(new BigDecimal(120.00))));

        Price p = new Price().setValue(new BigDecimal(140.00));
        List<String> errors = Validator.compareWithLowestBid(p, project);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(YOUR_BID_WAS_OUT_BID));
    }

    @Test
    public void validateWithBudget() throws Exception {
        Price p = new Price().setValue(new BigDecimal(199.99));
        List<String> errors = Validator.validPriceAgainstBudget(p, BackendTechAssessmentApplicationTests.mockProject());
        assertThat(errors, empty());
    }

    @Test
    public void invalidValidateWithBudget() throws Exception {
        Price p = new Price().setValue(new BigDecimal(500));
        List<String> errors = Validator.validPriceAgainstBudget(p, BackendTechAssessmentApplicationTests.mockProject());
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(BID_IS_GREATER_THAN_MAX_BUDGET_AMOUNT));
    }

    @Test
    public void validateBidDate() throws Exception {
        String closingDate = LocalDate.now().plus(1, ChronoUnit.DAYS).toString();
        List<String> errors = Validator.validBidDate(closingDate);
        assertThat(errors, empty());
    }

    @Test
    public void invalidValidateBidDate() throws Exception {
        String closingDate = "2018-05-15";
        List<String> errors = Validator.validBidDate(closingDate);
        assertThat(errors, not(empty()));
        assertThat(errors, hasItem(INVALID_BID_DATE));
    }


    @Test
    public void errorValidateBidDate() throws Exception {
        String closingDate = "efdjl3;";
        try {
            List<String> errors = Validator.validBidDate(closingDate);
        } catch (DateTimeParseException e) {
            assertNotNull(e);
        }
    }

}
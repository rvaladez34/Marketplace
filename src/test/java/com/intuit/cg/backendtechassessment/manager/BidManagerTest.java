package com.intuit.cg.backendtechassessment.manager;

import com.intuit.cg.backendtechassessment.BackendTechAssessmentApplicationTests;
import com.intuit.cg.backendtechassessment.model.Bid;
import com.intuit.cg.backendtechassessment.model.Person;
import com.intuit.cg.backendtechassessment.model.Price;
import com.intuit.cg.backendtechassessment.model.Project;
import com.intuit.cg.backendtechassessment.repository.MyRepository;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static com.intuit.cg.backendtechassessment.model.validator.ErrorMessages.BID_MUST_BE_LOWER_THAN_THE_CURRENT_LOWEST_BID;
import static com.intuit.cg.backendtechassessment.model.validator.ErrorMessages.YOUR_BID_WAS_OUT_BID;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

public class BidManagerTest {

    private BidManager bidManager;
    private MyRepository spyRepository;

    @Before
    public void setUp() throws Exception {
        //Setup spy for repository. We are only testing BidManger
        spyRepository = Mockito.mock(MyRepository.class);
        bidManager = new BidManager(spyRepository, 10);
    }

    @Test
    public void decrementBid() throws Exception {
        assertEquals(new BigDecimal(90.00), BidManager.decrementBid(new BigDecimal(100.00)));
        assertEquals(new BigDecimal(0.00), BidManager.decrementBid(new BigDecimal(10.00)));
        assertEquals(new BigDecimal(0.00), BidManager.decrementBid(new BigDecimal(0.00)));
        assertEquals(new BigDecimal(0.00), BidManager.decrementBid(new BigDecimal(-20.00)));
    }

    @Test
    public void formattedBidDecrement() throws Exception {
        assertEquals("$10.00", BidManager.formattedBidDecrement());
    }

    @Test
    @DisplayName("Process new fixed bid when it is first bid")
    public void processNewFixedBid() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAmount(new Price()
                        .setValue(new BigDecimal(150.00)));

        List<String> errors = bidManager.processNewBid(newBid, project).getValue();
        Mockito.verify(spyRepository).addBid(0, newBid);
        //assert no errors
        assertThat(errors, empty());
    }

    @Test
    @DisplayName("Process new auto bid when it is first bid")
    public void processNewAutoBid() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAutoBid(new Price()
                        .setValue(new BigDecimal(150.00)));

        List<String> errors = bidManager.processNewBid(newBid, project).getValue();
        Mockito.verify(spyRepository).addBid(0, newBid);
        //assert no errors
        assertThat(errors, empty());assertEquals(new BigDecimal(200), newBid.getAmount().getValue());
    }

    @Test
    @DisplayName("Process new fixed bid that is lower than the existing fixed bid")
    public void processFixedxFixedWinner() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        setBid("fixed", project);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAmount(new Price()
                        .setValue(new BigDecimal(150.00)));

        List<String> errors = bidManager.processNewBid(newBid, project).getValue();
        Mockito.verify(spyRepository).addBid(0, newBid);
        //assert no errors
        assertThat(errors, empty());
    }

    @Test
    @DisplayName("Process new fixed bid that is lower than the existing auto bid")
    public void processFixedxAutoWinner() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        setBid("auto", project);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAmount(new Price()
                        .setValue(new BigDecimal(150.00)));

        //current bid is at 200 and auto is at 160
        List<String> errors = bidManager.processNewBid(newBid, project).getValue();
        Mockito.verify(spyRepository).addBid(0, newBid);
        //assert no errors
        assertThat(errors, empty());
    }

    @Test
    @DisplayName("Process new auto bid that is lower than the existing fixed bid")
    public void processAutoxFixedWinner() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        setBid("fixed", project);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAutoBid(new Price()
                        .setValue(new BigDecimal(150.00)));

        List<String> errors = bidManager.processNewBid(newBid, project).getValue();
        Mockito.verify(spyRepository).addBid(0, newBid);
        //assert no errors
        assertThat(errors, empty());
    }

    @Test
    @DisplayName("Process new auto bid that is lower than the existing auto bid")
    public void processAutoxAutoWinner() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        setBid("auto", project);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAutoBid(new Price()
                        .setValue(new BigDecimal(150.00)));

        //current bid is at 200 and auto is at 160
        List<String> errors = bidManager.processNewBid(newBid, project).getValue();
        Mockito.verify(spyRepository).addBid(0, newBid);
        //assert no errors
        assertThat(errors, empty());
    }

    @Test
    @DisplayName("Process new fixed bid higher than current fixed bid")
    public void processFixedxFixedBeaten1() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        setBid("fixed", project);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAmount(new Price()
                        .setValue(new BigDecimal(180.00)));

        //current bid is at 160
        Pair<Project, List<String>> result = bidManager.processNewBid(newBid, project);
        Mockito.verify(spyRepository, times(0)).addBid(0, newBid);

        //assert errors
        List<String> errors = result.getValue();
        assertThat(errors, not(empty()));
        String expected =  BID_MUST_BE_LOWER_THAN_THE_CURRENT_LOWEST_BID.replace("{}", BidManager.formattedBidDecrement());
        assertThat(errors, contains(expected));

    }

    @Test
    @DisplayName("Process new fixed bid that does not meet the bid decrement amount")
    public void processFixedxFixedBeaten2() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        setBid("fixed", project);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAmount(new Price()
                        .setValue(new BigDecimal(155.00)));

        //current bid is at 160
        Pair<Project, List<String>> result = bidManager.processNewBid(newBid, project);
        Mockito.verify(spyRepository, times(0)).addBid(0, newBid);

        //assert errors
        List<String> errors = result.getValue();
        assertThat(errors, not(empty()));
        String expected =  BID_MUST_BE_LOWER_THAN_THE_CURRENT_LOWEST_BID.replace("{}", BidManager.formattedBidDecrement());
        assertThat(errors, contains(expected));
    }

    @Test
    @DisplayName("Process new auto bid higher than current fixed bid")
    public void processAutoxFixedBeaten1() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        setBid("fixed", project);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAutoBid(new Price()
                        .setValue(new BigDecimal(180.00)));

        //current bid is at 160
        Pair<Project, List<String>> result = bidManager.processNewBid(newBid, project);
        Mockito.verify(spyRepository, times(0)).addBid(0, newBid);

        //assert errors
        List<String> errors = result.getValue();
        assertThat(errors, not(empty()));
        String expected =  BID_MUST_BE_LOWER_THAN_THE_CURRENT_LOWEST_BID.replace("{}", BidManager.formattedBidDecrement());
        assertThat(errors, contains(expected));

    }

    @Test
    @DisplayName("Process new auto bid that does not meet the bid decrement amount")
    public void processAutoxFixedBeaten2() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        setBid("fixed", project);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAutoBid(new Price()
                        .setValue(new BigDecimal(155.00)));

        //current bid is at 160
        Pair<Project, List<String>> result = bidManager.processNewBid(newBid, project);
        Mockito.verify(spyRepository, times(0)).addBid(0, newBid);

        //assert errors
        List<String> errors = result.getValue();
        assertThat(errors, not(empty()));
        String expected =  BID_MUST_BE_LOWER_THAN_THE_CURRENT_LOWEST_BID.replace("{}", BidManager.formattedBidDecrement());
        assertThat(errors, contains(expected));
    }

    @Test
    @DisplayName("Process new fixed bid higher than current auto bid")
    public void processFixedxAutoBeaten1() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        setBid("auto", project);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAmount(new Price()
                        .setValue(new BigDecimal(180.00)));

        //current bid is at 200 and auto is at 160
        Pair<Project, List<String>> result = bidManager.processNewBid(newBid, project);
        Mockito.verify(spyRepository, times(0)).addBid(0, newBid);
        //check current bid is updated
        Mockito.verify(spyRepository, times(1)).updateCurrentBid(0, new BigDecimal(180));

        //assert errors
        List<String> errors = result.getValue();
        assertThat(errors, not(empty()));
        assertThat(errors, contains(YOUR_BID_WAS_OUT_BID));

    }

    @Test
    @DisplayName("Process new auto bid higher than current auto bid")
    public void processAutoxAutoBeaten1() throws Exception {
        Project project = BackendTechAssessmentApplicationTests.mockProject().setId(0);
        setBid("auto", project);
        Bid newBid = new Bid()
                .setBuyer(new Person()
                        .setName("Buyer")
                        .setEmail("12931@test.com"))
                .setAutoBid(new Price()
                        .setValue(new BigDecimal(180.00)));

        //current bid is at 200 and auto is at 160
        Pair<Project, List<String>> result = bidManager.processNewBid(newBid, project);
        Mockito.verify(spyRepository, times(0)).addBid(0, newBid);
        //check current bid is updated
        Mockito.verify(spyRepository, times(1)).updateCurrentBid(0, new BigDecimal(180));


        //assert errors
        List<String> errors = result.getValue();
        assertThat(errors, not(empty()));
        assertThat(errors, contains(YOUR_BID_WAS_OUT_BID));

    }

    private void setBid(String fixed, Project project) {
        BigDecimal maxBudget = project.getMaxBudget().getValue();
        Bid bid = new Bid().setBuyer(new Person().setName("Lowest Buyer").setEmail("lowest@test.com"));

        switch (fixed) {
            case "fixed" :
                bid.setAmount(new Price().setValue(maxBudget.subtract(new BigDecimal(40))));
                break;
            case "auto" :
                bid.setAmount(new Price().setValue(maxBudget.add(BigDecimal.ZERO)))
                        .setAutoBid(new Price().setValue(maxBudget.subtract(new BigDecimal(40))));
                break;
        }
        project.addBid(bid);
    }
}
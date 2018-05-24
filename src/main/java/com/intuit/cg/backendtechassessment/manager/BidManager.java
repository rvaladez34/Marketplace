package com.intuit.cg.backendtechassessment.manager;

import com.intuit.cg.backendtechassessment.model.Bid;
import com.intuit.cg.backendtechassessment.model.Price;
import com.intuit.cg.backendtechassessment.model.Project;
import com.intuit.cg.backendtechassessment.model.validator.Validator;
import com.intuit.cg.backendtechassessment.repository.MyRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.intuit.cg.backendtechassessment.model.validator.ErrorMessages.YOUR_BID_WAS_OUT_BID;

@Component
public class BidManager {

    private MyRepository repository;

    private static BigDecimal AUTO_DECREMENT_AMOUNT;

    @Autowired
    public BidManager(MyRepository repository,
                      @Value("${spring.bid.decrement}")int BID_DECREMENT) {
        this.repository = repository;
        AUTO_DECREMENT_AMOUNT = new BigDecimal(BID_DECREMENT);
    }

    /**
     * Method to compare new bid against current lowest bid
     *
     * If no valid bid data provided only fixed bid error will be returned as auto bid is optional
     *
     * @param newBid
     * @param project
     * @return
     */
    public Pair<Project, List<String>> processNewBid(Bid newBid, Project project) {
        Bid currentBid = project.getLowestBid();
        List<String> messages = new ArrayList<>();

        messages.addAll(Validator.validPerson(newBid.getBuyer()));
        messages.addAll(Validator.validBidDate(project.getClosingDate()));

        //return if invalid buyer or past closing date
        if (!messages.isEmpty())  {
            return new Pair<>(project,messages);
        }

        //determine if new bid is fixed or auto bid
        if (Validator.validPrice(newBid.getAutoBid(), "auto bid").isEmpty()) {
            /*
                process auto bid price
             */
            messages.addAll(Validator.validPriceAgainstBudget(newBid.getAutoBid(), project));
            if (null != currentBid) messages.addAll(Validator.compareWithLowestBid(newBid.getAutoBid(), project));

            if (messages.isEmpty()) {
                if (null == currentBid) {
                    //set the fixed price of the new bid to maxBudget
                    newBid.setAmount(new Price().setValue(project.getMaxBudget().getValue()));
                } else {
                    //set the fixed price of the new bid to the lowest price of current bid
                    newBid.setAmount(new Price().setValue(decrementBid(getLowestAmount(currentBid))));
                }
            }
        } else {
            messages.addAll(Validator.validPrice(newBid.getAmount(), "bid"));
            messages.addAll(Validator.validPriceAgainstBudget(newBid.getAmount(), project));
            /*
                process fixed bid price
             */
            if (messages.isEmpty() && null != currentBid)
                messages.addAll(Validator.compareWithLowestBid(newBid.getAmount(), project));
        }

        project = updateRepository(messages, project, currentBid, newBid);


        return new Pair<>(project,messages);
    }

    /**
     * Updates project in repository with new bid
     * or, if newBid was "out bid" then it updates project.lowestBid with new lower value
     *
     * @param messages errors from validation
     * @param project current project
     * @param currentBid project's lowest bid
     * @param newBid new bid to add to project
     * @return
     */
    private Project updateRepository(List<String> messages, Project project, Bid currentBid, Bid newBid) {
        Project updated = project;
        if (messages.isEmpty()) {
            updated = repository.addBid(project.getId(), newBid);
        } else if (null != currentBid
                && messages.contains(YOUR_BID_WAS_OUT_BID)) {
            //update current bid
            updated = repository
                    .updateCurrentBid(
                            project.getId(),
                            getLowestAmount(newBid).max(currentBid.getAutoBid().getValue()));
        }
        return updated;
    }


    /**
     * Get the lowest from Bid. That can be bid.amount or bid.autoBid
     * @param bid bid to compare
     * @return bid.autoBid or bid.amount
     */
    private BigDecimal getLowestAmount(Bid bid) {
        return null != bid.getAutoBid() ? bid.getAutoBid().getValue() : bid.getAmount().getValue();
    }

    /**
     * Returns the start amount decremented by the bid decrement amount or 0
     *
     * @param start value to decrement
     * @return start - {bid_decrement} OR ZERO
     */
    public static BigDecimal decrementBid(BigDecimal start) {
        return decrementBid(start, BigDecimal.ZERO);
    }

    /**
     * Returns the start amount decremented by the bid decrement amount or limit amount
     *
     * @param start value to decrement
     * @param limit start - {bid_decrement} OR limit
     * @return
     */
    private static BigDecimal decrementBid(BigDecimal start, BigDecimal limit) {
        return start.subtract(AUTO_DECREMENT_AMOUNT).max(limit);
    }

    /**
     * @return the bid decrement in $0.00 format
     */
    public static String formattedBidDecrement() {
        return NumberFormat.getCurrencyInstance(Locale.US).format(AUTO_DECREMENT_AMOUNT);
    }

    /**
     * Send copy of bid increment
     * @return new BigDecimal equal to bid decrement
     */
    public static BigDecimal getAutoDecrementAmount() {
        return AUTO_DECREMENT_AMOUNT.add(BigDecimal.ZERO);
    }

}

package com.intuit.cg.backendtechassessment.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Project extends BaseModel {

    private Integer id;
    private Person seller;
    private String description;
    private Price maxBudget;
    private String closingDate;
    private Bid lowestBid;
    private LinkedList<Bid> allBids;

    public Project() {

    }

    public Project(List<String> messages) {
        this.errors = messages;
    }

    public Project(String... messages) {
        this.addErrors(messages);
    }

    public Integer getId() {
        return id;
    }

    public Project setId(Integer id) {
        this.id = id;
        return this;
    }

    public Person getSeller() {
        return seller;
    }

    public Project setSeller(Person seller) {
        this.seller = seller;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }

    public Price getMaxBudget() {
        return maxBudget;
    }

    public Project setMaxBudget(Price maxBudget) {
        this.maxBudget = maxBudget;
        return this;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public Project setClosingDate(String closingDate) {
        this.closingDate = closingDate;
        return this;
    }

    public Bid getLowestBid() {
        return lowestBid;
    }

    public LinkedList<Bid> getAllBids() {
        return allBids;
    }

    public Project addBid(Bid bid) {
        //initialize set
        if (this.allBids == null) {
            this.allBids = new LinkedList<>();
            this.lowestBid = bid;
        }
        //add new bid
        this.allBids.push(bid);
        if (bid.getAmount().getValue().compareTo(this.lowestBid.getAmount().getValue()) < 0) {
            //match to lowest auto bid
            this.lowestBid = bid;
        }
        return this;
    }

    public Project setLowestBid(Bid lowestBid) {
        this.lowestBid = lowestBid;
        return this;
    }

    public Project setAllBids(LinkedList<Bid> allBids) {
        this.allBids = allBids;
        return this;
    }

    public Project copy() {
        Project copy = new Project();
        BeanUtils.copyProperties(this, copy);
        return copy;
    }
}

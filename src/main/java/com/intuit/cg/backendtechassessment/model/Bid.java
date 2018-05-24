package com.intuit.cg.backendtechassessment.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class Bid {

    Person buyer;
    Price amount;
    Price autoBid;

    public Person getBuyer() {
        return buyer;
    }

    public Bid setBuyer(Person buyer) {
        this.buyer = buyer;
        return this;
    }

    public Price getAmount() {
        return amount;
    }

    public Bid setAmount(Price amount) {
        this.amount = amount;
        return this;
    }

    public Price getAutoBid() {
        return autoBid;
    }

    public Bid setAutoBid(Price autoBid) {
        this.autoBid = autoBid;
        return this;
    }
}

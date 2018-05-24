package com.intuit.cg.backendtechassessment.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Price {

    private BigDecimal value;
    private String formated;

    public BigDecimal getValue() {
        return null != value ? value.add(BigDecimal.ZERO) : null;
    }

    public Price setValue(BigDecimal value) {
        if (null == value) { this.value = null; this.formated = null; return this; }

        this.value = value.add(BigDecimal.ZERO);
        //format value
        this.formated = NumberFormat.getCurrencyInstance(Locale.US).format(value);
        return this;
    }

    public String getFormated() {
        return formated;
    }

    public Price setFormated(String formated) {
        this.formated = formated;
        return this;
    }
}

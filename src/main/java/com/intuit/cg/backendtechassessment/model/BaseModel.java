package com.intuit.cg.backendtechassessment.model;

import java.util.Arrays;
import java.util.List;

public class BaseModel {

    protected List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addErrors(String... errors) {
        if (this.errors == null) {
            this.errors = Arrays.asList(errors);
        } else {
            this.errors.addAll(Arrays.asList(errors));
        }
    }
}

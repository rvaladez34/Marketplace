package com.intuit.cg.backendtechassessment.repository;

import com.intuit.cg.backendtechassessment.model.Bid;
import com.intuit.cg.backendtechassessment.model.Project;

import java.math.BigDecimal;

public interface MyRepository {

    public Project getProject(Integer id);

    public Project addProject(Project project);

    public Project addBid(Integer id, Bid bid);

    public Project updateCurrentBid(Integer id, BigDecimal newAmount);
}

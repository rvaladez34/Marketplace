package com.intuit.cg.backendtechassessment.controller;

import com.intuit.cg.backendtechassessment.BackendTechAssessmentApplicationTests;
import com.intuit.cg.backendtechassessment.manager.BidManager;
import com.intuit.cg.backendtechassessment.repository.MyRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;

public class BidsResourceTest {

    @Mock
    private BidManager bidManager;
    @Mock
    private MyRepository spyRepository;

    @InjectMocks
    private BidsResource bidsResource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        //mock repository
        Mockito.when(spyRepository.getProject(0)).thenReturn(BackendTechAssessmentApplicationTests.mockProject().setId(0));
        Mockito.when(spyRepository.getProject(1)).thenReturn(null);

        //mock bid manager
        Mockito.when(bidManager.processNewBid(any(), any())).thenReturn(null);
    }

    @Test
    public void submitSucessBid() throws Exception {

    }

}
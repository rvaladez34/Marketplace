package com.intuit.cg.backendtechassessment.controller;

import com.intuit.cg.backendtechassessment.manager.BidManager;
import com.intuit.cg.backendtechassessment.model.Bid;
import com.intuit.cg.backendtechassessment.model.Project;
import com.intuit.cg.backendtechassessment.controller.requestmappings.RequestMappings;
import com.intuit.cg.backendtechassessment.repository.MyRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.intuit.cg.backendtechassessment.model.validator.ErrorMessages.*;

@RestController
@RequestMapping(RequestMappings.PROJECTS + "/{projectId}" + RequestMappings.BIDS)
public class BidsResource {

    @Autowired
    MyRepository repository;

    @Autowired
    BidManager bidManager;

    /**
     * Adds new to bid to existing project
     *
     * @param projectId required in path
     * @param bid       required in body
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<Project> submitBid(@PathVariable int projectId, @RequestBody Bid bid) {
        ResponseEntity<Project> response;
        Project project = new Project();

        List<String> messages = new ArrayList<>();
        //validate projectId
        project = repository.getProject(projectId);
        if (null == project) {
            project = new Project();
            messages.add(PROJECT_ID_NOT_FOUND);
        } else {
            //validate bid
            Pair<Project, List<String>> result = bidManager.processNewBid(bid, project);
            project = result.getKey();
            messages.addAll(result.getValue());
        }

        //set errors
        if (!messages.isEmpty()) project.setErrors(messages);

        //process all errors
        if (null != project && CollectionUtils.isEmpty(project.getErrors())) {
            response = new ResponseEntity<Project>(project, HttpStatus.CREATED);
        } else if (messages.contains(YOUR_BID_WAS_OUT_BID)) {
            response = new ResponseEntity<Project>(project, HttpStatus.CONFLICT);
        } else {
            response = new ResponseEntity<Project>(project, HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}

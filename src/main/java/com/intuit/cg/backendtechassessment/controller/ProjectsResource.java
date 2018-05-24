package com.intuit.cg.backendtechassessment.controller;

import com.intuit.cg.backendtechassessment.model.Project;
import com.intuit.cg.backendtechassessment.model.validator.Validator;
import com.intuit.cg.backendtechassessment.controller.requestmappings.RequestMappings;
import com.intuit.cg.backendtechassessment.repository.MyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.intuit.cg.backendtechassessment.model.validator.ErrorMessages.*;

@RestController
@RequestMapping(RequestMappings.PROJECTS)
public class ProjectsResource {

    @Autowired
    MyRepository repository;

    /**
     * Get project by projectId
     *
     * @param projectId project id is required
     * @return requested project matching id
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable int projectId) {
        ResponseEntity<Project> response;
        Project project;

        //validate projectId
        project = repository.getProject(projectId);
        if (null == project) project = new Project(PROJECT_ID_NOT_FOUND);

        if (CollectionUtils.isEmpty(project.getErrors())) {
            response = new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(project, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Create new project and assign new projectId
     *
     * @param project required in body
     * @return saved project with new projectId
     */
    @PostMapping("/")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        ResponseEntity<Project> response;
        List<String> messages = Validator.validProject(project);
        Project result;
        if (messages.isEmpty()) {
            result = repository.addProject(project);
            response = new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            result = new Project(messages);
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

}

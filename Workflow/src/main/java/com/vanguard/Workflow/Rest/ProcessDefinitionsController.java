
package com.vanguard.Workflow.Rest;

import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessDefinitionMeta;
import org.activiti.api.process.model.payloads.GetProcessDefinitionsPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vanguard.Workflow.Exceptions.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * Used to interact with deployed process definitions
 *
 */
@RestController
@Tag(name = "ProcessDefinitions Controller", description = "This REST controller is used to interact with deployed process definitions")
public class ProcessDefinitionsController {
    private Logger logger = LoggerFactory.getLogger(ProcessDefinitionsController.class);

    @Autowired
    private ProcessRuntime processRuntime;
    
    @Autowired
    private RepositoryService respositoryService;

    @GetMapping("/process-definitions")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides all process Definitions")
    public List<ProcessDefinition> getProcessDefinitions() {
    	Page<ProcessDefinition> processDefinitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        logger.info("> Available Process definitions: " + processDefinitionPage.getTotalItems());
        for (ProcessDefinition pd : processDefinitionPage.getContent()) {
            logger.info("\t > Process definition: " + pd);
        }
        return processDefinitionPage.getContent();
    }
    
    
    
    @GetMapping("/process-definition")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides all process Definitions")
    public ResponseEntity<ProcessDefinition> getProcessDefinitionId(@RequestParam(value="processDefinitionId") String processDefinitionId) throws ResourceNotFoundException{
    	try {
    		ProcessDefinition processDefinition= processRuntime.processDefinition(processDefinitionId);
    		logger.info(" Process definition Id is : " + processDefinition.getId()); 
    		return ResponseEntity.ok().body(processDefinition);
    	}
    	catch(Exception e) {
    		throw new ResourceNotFoundException("Process definition not found for this id :: " + processDefinitionId);
    	}
    }
    
}

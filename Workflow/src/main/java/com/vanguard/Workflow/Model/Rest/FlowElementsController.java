package com.vanguard.Workflow.Model.Rest;

import java.util.List;

import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/bpmn/flowElements")
@Tag(name = "FlowElements Controller", description = "This REST controller is used to json of Activiti flowElements")
public class FlowElementsController {
    private Logger logger = LoggerFactory.getLogger(FlowElementsController.class);

   

    @GetMapping("/StartEvent")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Get StartEvent")
    public StartEvent getStartEvent() {
    	
        return new StartEvent();
    }
    
    @GetMapping("/UserTask")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Get User Task")
    public UserTask getUserTask() {
    	
        return new UserTask();
    }
    
    @GetMapping("/ServiceTask")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Get Service Task")
    public ServiceTask getServiceTask() {
    	
        return new ServiceTask();
    }
    
    @GetMapping("/SequenceFlow")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Get Sequence Flow")
    public SequenceFlow getSequenceFLow() {
    	
        return new SequenceFlow();
    }
    
    @GetMapping("/EndEvent")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Get End Event")
    public EndEvent getEndEvent() {
    	
        return new EndEvent();
    }
    
    
}

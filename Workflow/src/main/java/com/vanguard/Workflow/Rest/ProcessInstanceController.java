package com.vanguard.Workflow.Rest;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.ProcessInstance.ProcessInstanceStatus;
import org.activiti.api.process.model.ProcessInstanceMeta;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.RemoveProcessVariablesPayload;
import org.activiti.api.process.model.payloads.SetProcessVariablesPayload;
import org.activiti.api.process.model.payloads.GetVariablesPayload;
import org.activiti.api.process.runtime.ProcessAdminRuntime;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.runtime.shared.query.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vanguard.Workflow.Exceptions.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.nio.channels.Pipe;
import java.util.List;
/**
 * Used to get information about a process instance
 *
 */
@RestController
@Tag(name = "ProcessInstance Controller", description = "This REST controller is used to get information about a process instances")
public class ProcessInstanceController {
    private Logger logger = LoggerFactory.getLogger(ProcessInstanceController.class);
    @Autowired
    private ProcessRuntime processRuntime;
    
    @Autowired
    private ProcessAdminRuntime processAdminRuntime;
    @GetMapping("/process-instances")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides all process instances")
    public List<ProcessInstance> getProcessInstances() {
        List<ProcessInstance> processInstances =
                processRuntime.processInstances(Pageable.of(0, 10)).getContent();
        return processInstances;
    }

    
    @GetMapping("/process-instance-meta")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides Metadata of ProcessInstance")
    public ResponseEntity<ProcessInstanceMeta> getProcessInstanceMeta(@RequestParam(value="processInstanceId") String processInstanceId) throws ResourceNotFoundException {
    	try {
    		ProcessInstanceMeta processInstanceMeta = processRuntime.processInstanceMeta(processInstanceId);
    		return ResponseEntity.ok().body(processInstanceMeta);
    	}
    	catch(Exception e) {
    		throw new ResourceNotFoundException("Process Instance Meta not found for this id :: " + processInstanceId);
    	}
    }
    
    

    
    @GetMapping("/process-variables")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Get Process Variables")
    public List<VariableInstance> getProcessVariables(@RequestParam(value="processInstanceId") String processInstanceId) throws ResourceNotFoundException {
        try {
        	List<VariableInstance> variables= processRuntime.variables(
        			ProcessPayloadBuilder
	                        .variables()
	                        .withProcessInstanceId(processInstanceId)
	                        .build());   
        	return variables;
        }
        catch(Exception e) {
        	throw new ResourceNotFoundException("Process variables not found for this id :: " + processInstanceId);
        }
    }

    @PostMapping("/process-variables")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Set Process Variables")
    public String setProcessVariables(@RequestParam(value="processInstanceId") String processInstanceId, @RequestBody SetProcessVariablesPayload setProcessVariablesPayload ) {
        try{
        	processRuntime.setVariables(setProcessVariablesPayload);       
        }
        catch(Exception e) {
        	return new String("Process variables not found for this id :: " + processInstanceId);
        }
    	return "Process Variables Set";
    }
    

    
    @DeleteMapping("/process-variables")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Remove Process Variables")
    public String setProcessVariables(@RequestParam(value="processInstanceId") String processInstanceId,RemoveProcessVariablesPayload removeProcessVariablesPayload)throws ResourceNotFoundException {
        try{
        	processRuntime.removeVariables(removeProcessVariablesPayload);
        	return "Process Variables Set";
        }
        catch(Exception e) {
        	throw new ResourceNotFoundException("Process variables not found for this id :: " + processInstanceId);
        }
    }
    
    

    @DeleteMapping("/delete-processInstance")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Delete ProcessInstance")
    public String deleteProcessInstance(@RequestParam(value="processInstanceId") String processInstanceId) throws ResourceNotFoundException {
       try {
        String processInstanceStat = "NOT_CANCELLED";
        ProcessInstance deletedProcessInstance = processRuntime.delete(ProcessPayloadBuilder.delete(processInstanceId));
        if(deletedProcessInstance.getStatus().equals(ProcessInstanceStatus.CANCELLED)) {
            processInstanceStat = "CANCELLED";
        }
        
        return processInstanceStat;
       }
       catch(Exception e) {
       	throw new ResourceNotFoundException("ProcessInstance not found for this id :: " + processInstanceId);
       }
    }

    
    @PostMapping("/cleanup")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Cleanup As Admin")
    public String cleanUp() {
        Page<ProcessInstance> processInstancePage = processAdminRuntime.processInstances(Pageable.of(0, 50));
        for(ProcessInstance pi : processInstancePage.getContent()){
            processAdminRuntime.delete(ProcessPayloadBuilder.delete(pi.getId()));
        }
        return "Cleaned up successfully.";
    }
    
    
   
}
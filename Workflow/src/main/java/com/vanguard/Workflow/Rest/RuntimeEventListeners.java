package com.vanguard.Workflow.Rest;
import java.util.List;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.api.process.runtime.conf.ProcessRuntimeConfiguration;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
import org.activiti.api.runtime.shared.events.VariableEventListener;
import org.activiti.api.task.runtime.conf.TaskRuntimeConfiguration;
import org.activiti.api.task.runtime.events.listener.TaskRuntimeEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
public class RuntimeEventListeners {
    
    @Autowired
    private ProcessRuntime processRuntime;
    
    @Autowired
    private TaskRuntime taskRuntime;
    
     @GetMapping("/processRuntimeEventListeners")
     @ResponseStatus(code = HttpStatus.OK)
     @Operation(summary = "Process RuntimeEvent Listeners")
     public List<ProcessRuntimeEventListener<?>> processRunTimeEventListeners() {
         ProcessRuntimeConfiguration configuration = processRuntime.configuration();
         List<ProcessRuntimeEventListener<?>> processRuntimeEventListeners = configuration.processEventListeners();
         return processRuntimeEventListeners;
     }
     
     @GetMapping("/processVariableEventListeners")
     @ResponseStatus(code = HttpStatus.OK)
     @Operation(summary = "Process Variable EventListeners")
     public List<VariableEventListener<?>>  processVariableEventListeners() {
         ProcessRuntimeConfiguration configuration = processRuntime.configuration();
         List<VariableEventListener<?>> variableEventListeners = configuration.variableEventListeners();
         return variableEventListeners;
     }
     
     @GetMapping("/taskRuntimeEventListeners")
     @ResponseStatus(code = HttpStatus.OK)
     @Operation(summary = "Task Runtime EventListeners")
     public List<TaskRuntimeEventListener<?>>  taskRunTimeEventListeners() {
         TaskRuntimeConfiguration configuration = taskRuntime.configuration();
         List<TaskRuntimeEventListener<?>> taskRuntimeEventListeners = configuration.taskRuntimeEventListeners();
         return taskRuntimeEventListeners;
     }
     
     
}
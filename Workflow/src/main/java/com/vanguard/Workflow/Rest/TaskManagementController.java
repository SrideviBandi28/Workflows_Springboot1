/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vanguard.Workflow.Rest;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.model.payloads.SetTaskVariablesPayload;
import org.activiti.api.task.model.payloads.ClaimTaskPayload;
import org.activiti.api.task.model.payloads.DeleteTaskPayload;
import org.activiti.api.task.model.payloads.GetTaskVariablesPayload;
import org.activiti.api.task.model.payloads.ReleaseTaskPayload;
import org.activiti.api.task.runtime.TaskAdminRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.delegate.VariableScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vanguard.Workflow.Exceptions.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * Used to manage user tasks
 *
 */
@RestController
//@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api/v1")
@Tag(name = "TaskManagement Controller", description = "This REST controller is used to manage tasks")
public class TaskManagementController {
	private Logger logger = LoggerFactory.getLogger(TaskManagementController.class);

	@Autowired
	private TaskRuntime taskRuntime;

	@Autowired
	private TaskAdminRuntime taskAdminRuntime;

	@GetMapping("/my-tasks")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Provides all tasks of user in context")
	public List<Task> getMyTasks() {
		Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
		logger.info(" My Available Tasks: " + tasks.getTotalItems());

		for (Task task : tasks.getContent()) {
			logger.info(" My User Task: " + task);
		}

		return tasks.getContent();
	}

	/**
	 * Need to be logged in as admin to use this call
	 */
//	@CrossOrigin(origins = "*")
	@GetMapping("/all-tasks")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Provides all tasks, need admin user access to run this")
	public List<Task> getAllTasks() {
		Page<Task> tasks = taskAdminRuntime.tasks(Pageable.of(0, 10));
		logger.info(" All Available Tasks: " + tasks.getTotalItems());

		for (Task task : tasks.getContent()) {
			logger.info(" User Task: " + task);
		}

		return tasks.getContent();
	}

	@GetMapping("/complete-task")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Used to complete all tasks")
	public Task completeTask(@RequestParam(value = "taskId") String taskId)throws ResourceNotFoundException {
		try {
//			taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId).build());
//			logger.info("Completed Task: " + taskId);
//			return "Completed Task: " + taskId;
			Task completeTask = taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId).build());
			return completeTask;
		}catch(Exception e) {
			throw new ResourceNotFoundException("Task not found for this id : " + taskId);
		}
	}

	@DeleteMapping("/delete-task")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Used to Delete Task")
	public Task deleteTask(@RequestBody DeleteTaskPayload deleteTaskPayload){		
		Task taskDeleted = taskRuntime
				.delete(deleteTaskPayload);
		return taskDeleted;		
	}

	@PutMapping("/claimTask")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Used to Claim Task")
	public Task claimTask(@RequestBody ClaimTaskPayload claimTaskPayload) {
		Task claimedTask = taskRuntime.claim(claimTaskPayload);
		return claimedTask;
	}

	@PostMapping("/releaseTask")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Used Release Task")
	public Task releaseTask(@RequestBody ReleaseTaskPayload releaseTaskPayload) {
		Task releasedTask = taskRuntime.release(releaseTaskPayload);
		return releasedTask;
	}
	
	@PostMapping("/addTaskVariables")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Used To Add Task Variables")
	public String addVariablesForTask(@RequestBody SetTaskVariablesPayload setTaskVariablesPayload) {
		try {
			taskRuntime.setVariables(TaskPayloadBuilder.setVariables().withTaskId(setTaskVariablesPayload.getTaskId()).withVariables(setTaskVariablesPayload.getVariables()).build());
		}catch(Exception e) {
			return new String("Issue while setting variables");
		}
		return new String("Variables are being set for Task successfully" + setTaskVariablesPayload.getId());
	}
	
	@GetMapping("/getTaskVariables")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Used to get all Task Variables")
	public List<VariableInstance> getVariablesForTask(@RequestBody GetTaskVariablesPayload getTaskVariablesPayload) {
		List<VariableInstance> taskData = taskRuntime.variables(getTaskVariablesPayload);
		return taskData;
	}
}
	

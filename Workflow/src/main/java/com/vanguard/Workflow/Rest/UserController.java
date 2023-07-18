package com.vanguard.Workflow.Rest;
import java.util.List;
import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@Tag(name = "User Controller", description = "This REST controller is used to manage Users")
public class UserController {
	
    @Autowired
    private  UserGroupManager userGroupManager;
    @PostMapping("/userGroup")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Used to get UserGroups")
    public List<String> getUserGroups(@RequestParam(value="userName") String userName) {
    	
    	List<String> userGroupNames = userGroupManager.getUserGroups(userName);
    	return userGroupNames;
    }
}
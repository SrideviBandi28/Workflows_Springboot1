package com.vanguard.Workflow.Rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.vanguard.Workflow.Security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
public class SecurityController {
    
    @Autowired
    private SecurityUtil securityUtil;
    
        @PostMapping("/loginAs")
        @ResponseStatus(code = HttpStatus.OK)
        @Operation(summary = "Login as a user")
        public void loginAsUser(@RequestParam(value="username") String userName) {
            securityUtil.logInAs(userName);
            return;
        }
}
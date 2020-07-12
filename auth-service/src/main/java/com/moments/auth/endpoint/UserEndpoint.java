package com.moments.auth.endpoint;

import com.moments.auth.model.PO.User;
import com.moments.auth.model.UserAuthTemplate;
import com.moments.auth.model.request.RequestSchema;
import com.moments.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class UserEndpoint {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/list-all")
    public ResponseEntity<List<User>> queryAll() {
        List<User> users = userService.queryAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserAuthTemplate> getUserInfo() {
        UserAuthTemplate principal = (UserAuthTemplate)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(principal);
    }


}

package com.moments.auth.endpoint;

import com.moments.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserEndpoint {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/list-all")
    public ResponseEntity<List<User>> queryAll() {
        List<User> users = userService.queryAll();
        return ResponseEntity.ok(users);
    }
}

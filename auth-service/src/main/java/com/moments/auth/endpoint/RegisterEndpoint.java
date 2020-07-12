package com.moments.auth.endpoint;

import com.moments.auth.model.request.EmailRegisterRequest;
import com.moments.auth.model.request.NicknameRegisterRequest;
import com.moments.auth.model.request.RequestSchema;
import com.moments.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/register")
public class RegisterEndpoint {

    @Autowired
    private UserService userService;

    @PostMapping("/email")
    public ResponseEntity<String> accountRegister(@Valid @RequestBody EmailRegisterRequest request) {
        userService.accountRegister(request);
        return ResponseEntity.ok("注册成功！");
    }

    @PostMapping("/nickname")
    public ResponseEntity<String> accountRegister(@Valid @RequestBody NicknameRegisterRequest request) {
        userService.accountRegister(request);
        return ResponseEntity.ok("注册成功！");
    }
}

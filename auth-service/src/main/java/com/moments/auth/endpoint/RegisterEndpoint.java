package com.moments.auth.endpoint;

import com.moments.auth.model.request.EmailRegisterRequest;
import com.moments.auth.model.request.NicknameRegisterRequest;
import com.moments.auth.model.request.RequestSchema;
import com.moments.auth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Api
@RestController
@RequestMapping(value = "/register")
public class RegisterEndpoint {

    @Autowired
    private UserService userService;

    @PostMapping("/email")
    @ApiOperation(value = "邮箱注册")
    public ResponseEntity<String> accountRegister(@Valid @RequestBody EmailRegisterRequest request) throws MessagingException {
        userService.accountRegister(request);
        return ResponseEntity.ok("注册成功！");
    }

    @PostMapping("/nickname")
    @ApiOperation(value = "用户名注册")
    public ResponseEntity<String> accountRegister(@Valid @RequestBody NicknameRegisterRequest request) throws MessagingException {
        userService.accountRegister(request);
        return ResponseEntity.ok("注册成功！");
    }
}

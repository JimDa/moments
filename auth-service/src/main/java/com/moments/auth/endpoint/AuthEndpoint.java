package com.moments.auth.endpoint;

import com.moments.auth.mapper.UserAccountPoMapper;
import com.moments.auth.model.PO.UserAccountPo;
import com.moments.auth.model.example.UserAccountPoExample;
import com.moments.auth.model.exception.BadRequestException;
import com.moments.auth.model.request.CustomLoginRequest;
import com.moments.auth.model.request.LoginRequest;
import com.moments.auth.model.request.SignUpRequest;
import com.moments.auth.model.response.AliSmsResponse;
import com.moments.auth.model.response.ApiResponse;
import com.moments.auth.model.response.AuthResponse;
import com.moments.auth.security.CustomAuthenticationToken;
import com.moments.auth.security.TokenProvider;
import com.moments.auth.service.AliSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthEndpoint {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserAccountPoMapper userAccountPoMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AliSmsService aliSmsService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody CustomLoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new CustomAuthenticationToken(
                        loginRequest.getPrincipal(),
                        loginRequest.getCredentials(),
                        loginRequest.getType()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserAccountPoExample example = new UserAccountPoExample();
        example.createCriteria()
                .andEmailEqualTo(signUpRequest.getEmail());
        if (!CollectionUtils.isEmpty(userAccountPoMapper.selectByExample(example))) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        UserAccountPo user = new UserAccountPo();
        user.setUsername(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
//        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userAccountPoMapper.insertSelective(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }

    @GetMapping(value = "/verify-code")
    public ResponseEntity<String> getVerifyCode(@RequestParam String phoneNum) {
        ResponseEntity responseEntity;
        try {
            AliSmsResponse aliSmsResponse = aliSmsService.sendMessage(phoneNum);
            responseEntity = ResponseEntity
                    .ok("获取验证码成功！");
        } catch (Exception e) {
            responseEntity = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("获取验证码失败！");
        }
        return responseEntity;

    }

}

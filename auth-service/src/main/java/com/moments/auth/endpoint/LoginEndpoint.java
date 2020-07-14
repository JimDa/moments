package com.moments.auth.endpoint;

import com.moments.auth.model.request.CustomLoginRequest;
import com.moments.auth.security.CustomAuthenticationToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/custom")
public class LoginEndpoint {

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody CustomLoginRequest loginRequest) {
        CustomAuthenticationToken token = new CustomAuthenticationToken(loginRequest.getPrincipal(), loginRequest.getCredentials(), loginRequest.getType());
        Authentication authenticate = authenticationManager.authenticate(token);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticate);
        return ResponseEntity.ok(null);
    }
}

package com.moments.auth.endpoint;

import com.moments.auth.mapper.UserAccountPoMapper;
import com.moments.auth.model.PO.UserAccountPo;
import com.moments.auth.model.UserPrincipal;
import com.moments.auth.model.exception.ResourceNotFoundException;
import com.moments.auth.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserAccountPoMapper userAccountPoMapper;

    @GetMapping("/user/me")
    @ApiIgnore
    @PreAuthorize("hasRole('USER')")
    public UserAccountPo getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return Optional.ofNullable(userAccountPoMapper.selectByPrimaryKey(userPrincipal.getId().intValue()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}

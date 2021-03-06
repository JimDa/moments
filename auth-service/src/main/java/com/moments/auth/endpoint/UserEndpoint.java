package com.moments.auth.endpoint;

import com.moments.auth.model.PO.User;
import com.moments.auth.model.UserAuthTemplate;
import com.moments.auth.model.UserPrincipal;
import com.moments.auth.model.request.RequestSchema;
import com.moments.auth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping(value = "/api")
public class UserEndpoint {
    @Autowired
    private UserService userService;
//    @Autowired
//    private RedisTokenStore tokenStore;
//    @Autowired
//    @Qualifier(value = "tokenService")
//    private DefaultTokenServices defaultTokenServices;

    @GetMapping(value = "/user/list-all")
    @ApiIgnore
    public ResponseEntity<List<User>> queryAll() {
        List<User> users = userService.queryAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user-info")
    @ApiOperation(value = "用户基本信息")
    public ResponseEntity<UserPrincipal> getUserInfo() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(principal);
    }

//    @DeleteMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest, @RequestBody Map<String, String> tokens) {
//        Collection<OAuth2AccessToken> allTokens = tokenStore.findTokensByClientId("android-app");
//        allTokens.forEach(v -> {
//            System.out.println(String.format("accessToken:%s", v.getValue()));
//            System.out.println(String.format("refreshToken:%s", v.getRefreshToken().getValue()));
//        });
//        String authorization = httpServletRequest.getHeader("Authorization");
//        String accessToken = authorization.substring(7);
//
//        ResponseEntity responseEntity = ResponseEntity.ok("退出成功！");
//        try {
//            String refreshToken = tokens.get("refreshToken");
//            defaultTokenServices.revokeToken(accessToken);
//            tokenStore.removeRefreshToken(refreshToken);
//        } catch (Exception e) {
//            System.out.println("退出失败！！！");
//            responseEntity = ResponseEntity.badRequest().body("退出失败！");
//        }
//        return responseEntity;
//    }


}

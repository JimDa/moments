//package com.moments.auth.security;
//
//import com.moments.auth.model.UserAuthTemplate;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//
//@Component
//public class CustomTokenEnhancer implements TokenEnhancer {
//    @Override
//    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
//        HashMap<String, Object> map = new HashMap<>();
//        final Object obj = oAuth2Authentication.getUserAuthentication().getDetails();
//        if (obj instanceof UserAuthTemplate) {
//            UserAuthTemplate user = (UserAuthTemplate) obj;
//            map.put("user_id", user.getId());
//            map.put("user_name", user.getUsername());
//        }
//
//        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(map);
//        return oAuth2AccessToken;
//    }
//}

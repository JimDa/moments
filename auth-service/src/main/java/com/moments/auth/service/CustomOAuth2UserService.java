package com.moments.auth.service;

import com.moments.auth.mapper.UserAccountPoMapper;
import com.moments.auth.model.PO.UserAccountPo;
import com.moments.auth.model.UserPrincipal;
import com.moments.auth.model.example.UserAccountPoExample;
import com.moments.auth.model.oauth2.user.OAuth2UserInfo;
import com.moments.auth.model.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserAccountPoMapper userAccountPoMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        //可能是github出于隐私安全问题没有返回邮箱
//        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
//            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
//        }
        UserAccountPoExample example = new UserAccountPoExample();
        UserAccountPoExample.Criteria criteria = example.createCriteria();
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        switch (registrationId) {
            case "github":
                criteria.andGithubOpenidEqualTo(oAuth2UserInfo.getId());
                break;
            case "wechat":
                criteria.andWechatOpenidEqualTo(oAuth2UserInfo.getId());
                break;
        }
        Optional<UserAccountPo> userOptional = userAccountPoMapper.selectByExample(example)
                .stream()
                .findFirst();
        UserAccountPo user = new UserAccountPo();
        if (userOptional.isPresent()) {
            user = userOptional.get();
//            user = userOptional.get();
//            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
//                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
//                        user.getProvider() + " account. Please use your " + user.getProvider() +
//                        " account to login.");
//            }
//            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes(), registrationId);
    }

    private UserAccountPo registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        UserAccountPo user = new UserAccountPo();
        user.setUsername(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setCreator("system");
        user.setCreateDate(new Date());
        user.setAvatarUrl(oAuth2UserInfo.getImageUrl());
        switch (registrationId) {
            case "github":
                user.setGithubOpenid(oAuth2UserInfo.getId());
                break;
            case "wechat":
                user.setWechatOpenid(oAuth2UserInfo.getId());
        }
        userAccountPoMapper.insertSelective(user);
        return user;
    }

    private UserAccountPo updateExistingUser(UserAccountPo existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setUsername(oAuth2UserInfo.getName());
//        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        userAccountPoMapper.updateByPrimaryKeySelective(existingUser);
        return existingUser;
    }

}

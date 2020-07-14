package com.moments.auth.service.impl;

import com.moments.auth.mapper.UserAccountPoMapper;
import com.moments.auth.mapper.UserRoleRelPoMapper;
import com.moments.auth.model.PO.UserAccountPo;
import com.moments.auth.model.PO.UserRoleRelPo;
import com.moments.auth.model.UserAuthTemplate;
import com.moments.auth.model.UserPrincipal;
import com.moments.auth.model.example.UserAccountPoExample;
import com.moments.auth.model.exception.ResourceNotFoundException;
import com.moments.auth.model.exception.UserEmailNotFoundException;
import com.moments.auth.model.exception.UserPhoneNumNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserAccountPoMapper userAccountPoMapper;
    @Autowired
    private UserRoleRelPoMapper userRoleRelPoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccountPoExample userAccountPoExample = new UserAccountPoExample();
        userAccountPoExample.createCriteria()
                .andUsernameEqualTo(username);
        return userAccountPoMapper.selectByExample(userAccountPoExample)
                .stream()
                .map(v -> {
                    UserAuthTemplate template = new UserAuthTemplate();
                    template.setId(v.getId());
                    template.setUsername(v.getUsername());
                    template.setPassword(v.getPassword());
                    return template;
                })
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException(String.format("cannot find account of username: %s!", username)));
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {

        UserAccountPo user = Optional.ofNullable(userAccountPoMapper.selectByPrimaryKey(id.intValue()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserPrincipal.create(user);
    }

    public UserDetails loadByEmail(String email) throws UserEmailNotFoundException {
        UserAccountPoExample userAccountPoExample = new UserAccountPoExample();
        userAccountPoExample.createCriteria()
                .andEmailEqualTo(email);
        return userAccountPoMapper.selectByExample(userAccountPoExample)
                .stream()
                .map(v -> {
                    UserAuthTemplate template = new UserAuthTemplate();
                    template.setId(v.getId());
                    template.setUsername(v.getUsername());
                    template.setPassword(v.getPassword());
                    return template;
                })
                .findFirst()
                .orElseThrow(() -> new UserEmailNotFoundException(String.format("cannot find account of email: %s!", email)));
    }

    public UserDetails loadByPhoneNum(String phoneNum) throws UserPhoneNumNotFoundException {
        UserAuthTemplate authTemplate = new UserAuthTemplate();

        UserAccountPoExample userAccountPoExample = new UserAccountPoExample();
        userAccountPoExample.createCriteria()
                .andPhoneNumEqualTo(phoneNum);
        List<UserAccountPo> userAccountPos = userAccountPoMapper.selectByExample(userAccountPoExample);
        if (CollectionUtils.isEmpty(userAccountPos)) {
            UserAccountPo userAccountPo = new UserAccountPo();
            userAccountPo.setPhoneNum(phoneNum);
            userAccountPo.setCreateDate(new Date());
            userAccountPo.setCreator("system");
            userAccountPoMapper.insertSelective(userAccountPo);
            UserRoleRelPo userRoleRelPo = new UserRoleRelPo();
            userRoleRelPo.setUserId(userAccountPo.getId());
            userRoleRelPo.setRoleId(2);
            userRoleRelPoMapper.insertSelective(userRoleRelPo);
            authTemplate.setId(userAccountPo.getId());
            authTemplate.setUsername(phoneNum);

        } else {
            authTemplate = userAccountPos.stream()
                    .map(v -> {
                        UserAuthTemplate template = new UserAuthTemplate();
                        template.setId(v.getId());
                        template.setUsername(v.getUsername());
                        template.setPassword(v.getPassword());
                        return template;
                    })
                    .findFirst()
                    .orElseThrow(() -> new UserPhoneNumNotFoundException(String.format("cannot find account of phone: %s!", phoneNum)));
        }
        return authTemplate;
    }
}

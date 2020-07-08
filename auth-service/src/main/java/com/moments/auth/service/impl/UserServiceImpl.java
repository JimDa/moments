package com.moments.auth.service.impl;

import com.moments.auth.dao.UserMapper;
import com.moments.auth.model.User;
import com.moments.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryAll() {
        return userMapper.selectByExample(null);
    }
}

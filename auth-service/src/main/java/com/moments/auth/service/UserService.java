package com.moments.auth.service;

import com.moments.auth.model.PO.User;
import com.moments.auth.model.request.RequestSchema;

import java.util.List;

public interface UserService {
    List<User> queryAll();

    void accountRegister(RequestSchema request);
}

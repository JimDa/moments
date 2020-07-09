package com.moments.auth.model.exception;

import org.springframework.security.core.AuthenticationException;

public class UserPhoneNumNotFoundException extends AuthenticationException {
    public UserPhoneNumNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserPhoneNumNotFoundException(String msg) {
        super(msg);
    }
}

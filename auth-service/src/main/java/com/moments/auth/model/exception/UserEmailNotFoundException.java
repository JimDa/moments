package com.moments.auth.model.exception;

import org.springframework.security.core.AuthenticationException;

public class UserEmailNotFoundException extends AuthenticationException {
    public UserEmailNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserEmailNotFoundException(String msg) {
        super(msg);
    }
}

package com.moments.auth.service;

import com.moments.auth.model.response.AliSmsResponse;

public interface AliSmsService {
    AliSmsResponse sendMessage(String phoneNum) throws Exception;
}

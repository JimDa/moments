package com.moments.auth.model.request;

import lombok.Data;

@Data
public class CustomLoginRequest {
    private String type;
    private String principal;
    private String credentials;
}

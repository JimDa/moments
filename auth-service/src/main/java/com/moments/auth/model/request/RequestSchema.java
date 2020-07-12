package com.moments.auth.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSchema {
    @NotBlank(message = "请求类型不能为空")
    private String type;
    @NotBlank(message = "密码不能为空！")
    private String password;
}

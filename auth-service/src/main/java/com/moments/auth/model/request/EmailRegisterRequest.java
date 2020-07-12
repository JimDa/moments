package com.moments.auth.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRegisterRequest extends RequestSchema{
    @NotBlank(message = "邮箱地址不能为空！")
    @Email(message = "邮箱地址格式有误！")
    private String email;
}

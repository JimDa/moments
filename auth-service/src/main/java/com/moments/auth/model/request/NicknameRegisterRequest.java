package com.moments.auth.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NicknameRegisterRequest extends RequestSchema{
    @NotBlank(message = "昵称不能为空")
    private String nickname;
}

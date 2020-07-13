package com.moments.auth.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AliSmsResponse {
    private String Message = "调用阿里云sms服务失败！";
    private String RequestId;
    private String BizId;
    private String Code = "500";
}

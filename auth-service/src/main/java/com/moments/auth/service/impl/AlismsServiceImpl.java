package com.moments.auth.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.moments.auth.mapper.ThirdPartyAccessPoMapper;
import com.moments.auth.model.PO.ThirdPartyAccessPo;
import com.moments.auth.model.example.ThirdPartyAccessPoExample;
import com.moments.auth.model.response.AliSmsResponse;
import com.moments.auth.service.AliSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AlismsServiceImpl implements AliSmsService {
    @Autowired
    private Gson gson = new Gson();
    @Autowired
    private ThirdPartyAccessPoMapper thirdPartyAccessPoMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public AliSmsResponse sendMessage(String phoneNum) {
        ThirdPartyAccessPoExample example = new ThirdPartyAccessPoExample();
        example.createCriteria()
                .andServiceNameEqualTo("aliSms");
        AliSmsResponse result = new AliSmsResponse();
        Optional<ThirdPartyAccessPo> first = thirdPartyAccessPoMapper.selectByExample(example)
                .stream()
                .findFirst();
        if (first.isPresent()) {
            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", first.get().getAccessKey(), first.get().getAccessSecret());
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            request.putQueryParameter("PhoneNumbers", phoneNum);
            request.putQueryParameter("SignName", "ezblog短信验证");
            request.putQueryParameter("TemplateCode", "SMS_175572254");
            String verifyCode = getRandomCode();
            request.putQueryParameter("TemplateParam", String.format("{\"code\":\"%s\"}", verifyCode));
            String message = null;
            try {
                CommonResponse response = client.getCommonResponse(request);
                System.out.println(response.getData());
                message = response.getData();
                stringRedisTemplate.opsForValue().set("ALI_SMS:".concat(phoneNum), verifyCode, 5, TimeUnit.MINUTES);
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
            result = StringUtils.isEmpty(message) ? result : gson.fromJson(message, AliSmsResponse.class);
        }
        return result;
    }

    private String getRandomCode() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}

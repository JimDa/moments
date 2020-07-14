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
import com.moments.auth.mapper.SmsTemplatePoMapper;
import com.moments.auth.mapper.ThirdPartyAccessPoMapper;
import com.moments.auth.model.PO.SmsTemplatePo;
import com.moments.auth.model.PO.ThirdPartyAccessPo;
import com.moments.auth.model.example.SmsTemplatePoExample;
import com.moments.auth.model.example.ThirdPartyAccessPoExample;
import com.moments.auth.model.response.AliSmsResponse;
import com.moments.auth.service.AliSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AliSmsServiceImpl implements AliSmsService {
    @Autowired
    private Gson gson = new Gson();
    @Autowired
    private ThirdPartyAccessPoMapper thirdPartyAccessPoMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SmsTemplatePoMapper smsTemplatePoMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AliSmsResponse sendMessage(String phoneNum) throws Exception {
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
            SmsTemplatePoExample smsTemplatePoExample = new SmsTemplatePoExample();
            smsTemplatePoExample.createCriteria()
                    .andTemplateTypeEqualTo("验证码");
            Optional<SmsTemplatePo> templatePo = smsTemplatePoMapper.selectByExample(smsTemplatePoExample)
                    .stream()
                    .findFirst();
            if (templatePo.isPresent()) {
                request.putQueryParameter("SignName", "ezblog短信验证");
                request.putQueryParameter("TemplateCode", templatePo.get().getTemplateCode());
                String verifyCode = getRandomCode();
                request.putQueryParameter("TemplateParam", String.format("{\"code\":\"%s\"}", verifyCode));
                String message = null;
                try {
                    CommonResponse response = client.getCommonResponse(request);
                    System.out.println(response.getData());
                    message = response.getData();
                    String encodedVerifyCode = bCryptPasswordEncoder.encode(verifyCode);
                    stringRedisTemplate.opsForValue().set("ALI_SMS:".concat(phoneNum), encodedVerifyCode, 1, TimeUnit.MINUTES);
                } catch (ServerException e) {
                    e.printStackTrace();
                    throw new ServerException("500", result.getMessage());
                } catch (ClientException e) {
                    e.printStackTrace();
                    throw new ServerException("500", result.getMessage());
                }
                result = StringUtils.isEmpty(message) ? result : gson.fromJson(message, AliSmsResponse.class);
            }
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

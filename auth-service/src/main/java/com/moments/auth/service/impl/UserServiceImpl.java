package com.moments.auth.service.impl;


import com.moments.auth.mapper.UserAccountPoMapper;
import com.moments.auth.mapper.UserMapper;
import com.moments.auth.model.PO.User;
import com.moments.auth.model.PO.UserAccountPo;
import com.moments.auth.model.request.EmailRegisterRequest;
import com.moments.auth.model.request.NicknameRegisterRequest;
import com.moments.auth.model.request.RequestSchema;
import com.moments.auth.service.UserService;
import com.moments.auth.util.MailSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAccountPoMapper userAccountPoMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Override
    public List<User> queryAll() {
        return userMapper.selectByExample(null);
    }

    @Override
    public void accountRegister(RequestSchema request) throws MessagingException {
        String type = request.getType();
        UserAccountPo account = new UserAccountPo();
        Date now = new Date();
        account.setCreateDate(now);
        account.setCreator("system");

        switch (type) {
            case "email":
                EmailRegisterRequest emailRegisterRequest = (EmailRegisterRequest) request;
                account.setEmail(emailRegisterRequest.getEmail());
                account.setPassword(passwordEncoder.encode(emailRegisterRequest.getPassword()));
                String msg = String.format("恭喜%s注册成功！", emailRegisterRequest.getEmail());
                String email = emailRegisterRequest.getEmail();
                String subject = "注册提醒";
                mailSenderUtil.sendMsgTo(msg, email, subject);
                break;
            case "nickname":
                NicknameRegisterRequest nicknameRegisterRequest = (NicknameRegisterRequest) request;
                account.setEmail(nicknameRegisterRequest.getNickname());
                account.setPassword(passwordEncoder.encode(nicknameRegisterRequest.getPassword()));
                break;
        }
        userAccountPoMapper.insertSelective(account);
    }
}

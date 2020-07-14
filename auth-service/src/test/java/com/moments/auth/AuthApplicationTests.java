package com.moments.auth;

import com.moments.auth.model.request.EmailRegisterRequest;
import com.moments.auth.model.response.AliSmsResponse;
import com.moments.auth.service.AliSmsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthApplicationTests {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private AliSmsService aliSmsService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void sendEmail() throws MessagingException {
        EmailRegisterRequest request = new EmailRegisterRequest();
        request.setType("email");
        request.setEmail("dapengcheng2011@hotmail.com");
        request.setPassword("123");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("1085529137@qq.com");
        simpleMailMessage.setText(String.format("恭喜%s注册成功！", request.getEmail()));
        simpleMailMessage.setSubject("注册提醒");
        simpleMailMessage.setTo("dapengcheng2017@gmail.com");
        javaMailSender.send(simpleMailMessage);
    }

    @Test
    public void sendMsg() throws Exception {
        AliSmsResponse aliSmsResponse = aliSmsService.sendMessage("13817176315");
        System.out.println(aliSmsResponse);
    }

}

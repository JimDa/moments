package com.moments.auth;

import com.moments.auth.model.request.EmailRegisterRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    public void contextLoads() {
    }

    @Test
    public void sendEmail() throws MessagingException {
        EmailRegisterRequest request = new EmailRegisterRequest();
        request.setType("email");
        request.setEmail("xxxxxx@hotmail.com");
        request.setPassword("123");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(request.getEmail());
        helper.setSubject("注册提醒");
        helper.setText(String.format("恭喜%s注册成功！", request.getEmail()));
        javaMailSender.send(mimeMessage);
    }

}

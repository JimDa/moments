package com.moments.auth.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSenderUtil {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMsgTo(String msg, String address, String subject) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("1085529137@qq.com");
        simpleMailMessage.setText(msg);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setTo(address);
        javaMailSender.send(simpleMailMessage);
    }
}

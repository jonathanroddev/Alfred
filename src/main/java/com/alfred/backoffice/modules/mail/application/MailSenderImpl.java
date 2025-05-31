package com.alfred.backoffice.modules.mail.application;

import com.alfred.backoffice.modules.mail.domain.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    // TODO: Check if mail can be configured by passing it a token instead of the password

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private final String adminMail;

    @Override
    public void sendGenericMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendMailToAdmin(String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminMail);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

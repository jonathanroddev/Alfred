package com.alfred.backoffice.modules.mail.application;

import com.alfred.backoffice.modules.mail.domain.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    // TODO: Check if mail can be configured by passing it a token instead of the password

    private final JavaMailSender mailSender;

    @Override
    public void sendPasswordResetMail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Restablece tu contraseña");
        message.setText("Hola, puedes restablecer tu contraseña en este enlace:\n" + resetLink);
        mailSender.send(message);
    }
}

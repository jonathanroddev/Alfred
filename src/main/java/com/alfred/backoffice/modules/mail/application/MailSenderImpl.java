package com.alfred.backoffice.modules.mail.application;

import com.alfred.backoffice.modules.mail.domain.MailSender;
import com.alfred.backoffice.modules.mail.infrastructure.configuration.MailProperties;
import com.alfred.backoffice.modules.mail.infrastructure.configuration.OAuth2Properties;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final OAuth2Properties oAuth2Properties;
    private final Session session;

    @Override
    public void sendGenericMail(String to, String subject, String text) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(session.getProperty("mail.smtp.user")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
    }

    @Override
    public void sendMailToAdmin(String subject, String text) throws MessagingException {
        this.sendGenericMail(oAuth2Properties.getEmail(), subject, text);
    }
}

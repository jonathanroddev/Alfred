package com.alfred.backoffice.modules.mail.domain;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

public interface MailSender {
    void sendGenericMail(String to, String subject, String text) throws MessagingException;
    void sendMailToAdmin(String subject, String text) throws MessagingException;
}

package com.alfred.backoffice.modules.mail.domain;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

import java.util.Map;

public interface MailSender {
    void sendGenericMail(String to, String subject, String templateName, Map<String, Object> vars) throws MessagingException;
    void sendMailToAdmin(String subject, String templateName, Map<String, Object> vars) throws MessagingException;
}

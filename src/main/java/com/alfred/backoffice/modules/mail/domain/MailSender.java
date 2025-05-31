package com.alfred.backoffice.modules.mail.domain;

public interface MailSender {
    void sendGenericMail(String to, String subject, String text);
    void sendMailToAdmin(String subject, String text);
}

package com.alfred.backoffice.modules.mail.domain;

public interface MailSender {
    void sendPasswordResetMail(String to, String resetLink);
}

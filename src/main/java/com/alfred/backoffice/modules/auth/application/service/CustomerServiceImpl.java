package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.request.Registry;
import com.alfred.backoffice.modules.auth.domain.service.CustomerService;
import com.alfred.backoffice.modules.mail.domain.MailSender;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final MailSender mailSender;

    @Override
    public void askJoin(Registry registry) {
        String subject = "Solicitud de nuevo registro";
        String text = "Solicitante: " + registry.getMail() + ". \nComentario: " + registry.getComment();
        // TODO: Handle exception
        try {
            this.mailSender.sendMailToAdmin(subject, text);
        } catch (MessagingException me) {
            return;
        }
    }
}

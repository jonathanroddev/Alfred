package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.request.Registry;
import com.alfred.backoffice.modules.auth.domain.exception.BadGatewayException;
import com.alfred.backoffice.modules.auth.domain.service.CustomerService;
import com.alfred.backoffice.modules.mail.domain.MailSender;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final MailSender mailSender;
    private static final Logger logger =  LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public void askJoin(Registry registry) {
        // TODO: Send html instead
        String subject = "Solicitud de nuevo registro";
        String text = "Solicitante: " + registry.getMail() + ". \nComentario: " + registry.getComment();
        try {
            this.mailSender.sendMailToAdmin(subject, text);
        } catch (MessagingException me) {
            logger.error("Error sending mail to: admin. Details: {}", me.toString());
            throw new BadGatewayException("amg-502_4");
        }
    }
}

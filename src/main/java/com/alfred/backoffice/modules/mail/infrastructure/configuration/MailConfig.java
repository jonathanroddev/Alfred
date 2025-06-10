package com.alfred.backoffice.modules.mail.infrastructure.configuration;

import com.alfred.backoffice.modules.mail.infrastructure.external.gmail.service.GmailTokenProvider;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties({MailProperties.class, OAuth2Properties.class})
public class MailConfig {

    @Bean
    public Session mailSession(MailProperties mailProps, OAuth2Properties oauthProps) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.user", mailProps.getUser());
        props.put("mail.smtp.auth", mailProps.isAuth());
        props.put("mail.smtp.auth.mechanisms", mailProps.getAuthMechanisms());
        props.put("mail.smtp.starttls.enable", mailProps.isStarttlsEnable());
        props.put("mail.smtp.host", mailProps.getHost());
        props.put("mail.smtp.port", mailProps.getPort());

        String accessToken = GmailTokenProvider.getAccessToken(
                oauthProps.getClientId(),
                oauthProps.getClientSecret(),
                oauthProps.getRefreshToken()
        );

        return Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(oauthProps.getEmail(), accessToken);
            }
        });
    }
}

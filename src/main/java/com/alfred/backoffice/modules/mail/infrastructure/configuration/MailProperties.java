package com.alfred.backoffice.modules.mail.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "mail.smtp")
public class MailProperties {
    private String user;
    private boolean auth;
    private String authMechanisms;
    private boolean starttlsEnable;
    private String host;
    private int port;
}

package com.alfred.backoffice.modules.mail.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "mail.oauth2")
public class OAuth2Properties {
    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private String email;
}

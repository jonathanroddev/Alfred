package com.alfred.backoffice.modules.auth.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "error")
public class ErrorMessageProperties {
    private Map<String, String> messages = new HashMap<>();

    public String getMessage(String code) {
        return messages.getOrDefault(code, "Unknown error.");
    }

}

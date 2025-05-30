package com.alfred.backoffice.modules.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Registry {
    // TODO: Add mail validation
    private String mail;
    private String comment;
}

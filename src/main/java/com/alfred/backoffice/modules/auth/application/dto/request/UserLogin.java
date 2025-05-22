package com.alfred.backoffice.modules.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserLogin {
    private String email;
    private String password;
}

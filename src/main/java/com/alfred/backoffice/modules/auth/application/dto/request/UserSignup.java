package com.alfred.backoffice.modules.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserSignup {
    private String mail;
    private String password;
    private String communityId;
}

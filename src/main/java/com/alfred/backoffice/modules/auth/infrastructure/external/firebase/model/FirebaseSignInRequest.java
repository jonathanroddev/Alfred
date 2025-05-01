package com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FirebaseSignInRequest {
    private String email;
    private String password;
    private boolean returnSecureToken;
}

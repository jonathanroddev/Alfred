package com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FirebaseSignInResponse {
    private String idToken;
    private String refreshToken;
    private String localId;
}

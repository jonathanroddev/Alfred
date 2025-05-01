package com.alfred.backoffice.modules.auth.application.dto.response;

import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model.FirebaseSignInResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class UserLoginResponse {
    private FirebaseSignInResponse firebaseSignInResponse;
    private List<User> users;
}
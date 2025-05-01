package com.alfred.backoffice.modules.auth.infrastructure.external.firebase.service;

import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model.FirebaseSignInRequest;
import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model.FirebaseSignInResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class FirebaseService {

    private static final String DUPLICATE_ACCOUNT_ERROR = "EMAIL_EXISTS";

    public String createUser(String emailId, String password) throws Exception {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest();
        request.setEmail(emailId);
        request.setPassword(password);
        request.setEmailVerified(Boolean.TRUE);

        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            UserRecord userRecord = firebaseAuth.createUser(request);
            return userRecord.getUid();
        } catch (FirebaseAuthException exception) {
            if (exception.getMessage().contains(DUPLICATE_ACCOUNT_ERROR)) {
                // TODO: Throw custom exception. Extend of RuntimeException
                throw new Exception("Account with given email-id already exists");
            }
            throw exception;
        }
    }

    private static final String API_KEY_PARAM = "key";
    private static final String INVALID_CREDENTIALS_ERROR = "INVALID_LOGIN_CREDENTIALS";
    private static final String SIGN_IN_BASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";

    @Value("${firebase.api-key}")
    private String webApiKey;

    public FirebaseSignInResponse login(String emailId, String password) throws Exception {
        FirebaseSignInRequest requestBody = new FirebaseSignInRequest(emailId, password, true);
        return this.sendSignInRequest(requestBody);
    }

    private FirebaseSignInResponse sendSignInRequest(FirebaseSignInRequest firebaseSignInRequest) throws Exception {
        try {
            return RestClient.create(SIGN_IN_BASE_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam(API_KEY_PARAM, webApiKey)
                            .build())
                    .body(firebaseSignInRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(FirebaseSignInResponse.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getResponseBodyAsString().contains(INVALID_CREDENTIALS_ERROR)) {
                // TODO: Throw custom exception. Extend of RuntimeException
                throw new Exception();
            }
            throw exception;
        }
    }
}

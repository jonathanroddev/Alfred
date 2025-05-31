package com.alfred.backoffice.modules.auth.infrastructure.external.firebase.service;

import com.alfred.backoffice.modules.auth.domain.exception.BadGatewayException;
import com.alfred.backoffice.modules.auth.domain.exception.BadRequestException;
import com.alfred.backoffice.modules.auth.domain.exception.ConflictException;
import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model.FirebaseSignInRequest;
import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model.FirebaseSignInResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class FirebaseService {

    private static final String DUPLICATE_ACCOUNT_ERROR = "EMAIL_EXISTS";
    private static final String API_KEY_PARAM = "key";
    private static final String INVALID_CREDENTIALS_ERROR = "INVALID_LOGIN_CREDENTIALS";
    private static final String SIGN_IN_BASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";
    private static final Logger logger =  LoggerFactory.getLogger(FirebaseService.class);


    @Value("${firebase.api-key}")
    private String webApiKey;

    public String createUser(String email, String password) throws ConflictException, BadGatewayException, FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setEmailVerified(Boolean.FALSE);
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            UserRecord userRecord = firebaseAuth.createUser(request);
            return userRecord.getUid();
        } catch (FirebaseAuthException exception) {
            logger.error(exception.getMessage());
            if (exception.getMessage().contains(DUPLICATE_ACCOUNT_ERROR)) {
                UserRecord userRecord = this.getUserRecord(email);
                throw new ConflictException(userRecord.getUid());
            }
            logger.error("Firebase auth sign up error", exception);
            throw new BadGatewayException("amg-502_1");
        }
    }

    public String getResetPasswordLink(String mail) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().generatePasswordResetLink(mail);
    }

    public FirebaseSignInResponse login(String emailId, String password) throws BadRequestException, BadGatewayException {
        FirebaseSignInRequest requestBody = new FirebaseSignInRequest(emailId, password, true);
        return this.sendSignInRequest(requestBody);
    }

    private FirebaseSignInResponse sendSignInRequest(FirebaseSignInRequest firebaseSignInRequest) throws BadRequestException, BadGatewayException {
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
                throw new BadRequestException("amg-400_1");
            }
            logger.error("Firebase auth login error", exception);
            throw new BadGatewayException("amg-502_2");
        }
    }

    private UserRecord getUserRecord(String mail) throws FirebaseAuthException {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getUserByEmail(mail);
    }
}

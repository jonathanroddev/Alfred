package com.alfred.backoffice.modules.auth.infrastructure.external.firebase.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

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
                // TODO: Throw custom exception
                throw new Exception("Account with given email-id already exists");
            }
            throw exception;
        }
    }
}

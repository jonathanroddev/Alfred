package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.domain.model.User;

import java.util.UUID;


public interface UserService {
    void signup(UserSignup userSignup);
    void createUser(UserSignup userSignup) throws Exception;
    User getUser(UUID uuid) throws Exception;
    User getUser(String externalUuid) throws Exception;
}

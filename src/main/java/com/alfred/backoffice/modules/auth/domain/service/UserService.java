package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.request.UserSignup;
import com.alfred.backoffice.modules.auth.domain.model.Community;

public interface UserService {
    void signup(UserSignup userSignup) throws Exception;
    void createUser(String mail, Community community) throws Exception;
}

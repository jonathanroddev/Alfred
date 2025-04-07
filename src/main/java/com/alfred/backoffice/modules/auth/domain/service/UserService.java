package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.domain.model.UserStatus;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;

import java.util.UUID;


public interface UserService {
    void signup(UserSignup userSignup);
    void createUser(UserSignup userSignup) throws Exception;
    UserEntity getUserEntity(UUID uuid) throws Exception;
    User getUser(UUID uuid);
    User getUser(String externalUuid) throws Exception;
    void updateStatusOfUser(String uuid, UserStatusDTO userStatusDTO);
}

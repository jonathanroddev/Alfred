package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.UserTypeDTO;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;


public interface UserService {
    boolean isActive(User user);
    boolean hasLevel(User user, int level);
    boolean hasAuth(Authentication authentication, int level);
    void signup(UserSignup userSignup);
    void createUser(UserSignup userSignup) throws Exception;
    UserEntity getUserEntity(UUID uuid) throws Exception;
    User getUser(UUID uuid);
    User getUserByExternalUuid(String externalUuid) throws Exception;
    List<UserTypeDTO> getAllUserTypesFilterByAuth(Authentication authentication) throws Exception;
    void updateStatusOfUser(String uuid, UserStatusDTO userStatusDTO) throws Exception;
    void addTypeOfUser(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws Exception;
    void deleteTypeOfUser(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws Exception;
}

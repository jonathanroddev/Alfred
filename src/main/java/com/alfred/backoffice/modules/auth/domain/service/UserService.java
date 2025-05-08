package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.request.UserLogin;
import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.application.dto.response.UserLoginResponse;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.UserTypeDTO;
import com.alfred.backoffice.modules.auth.domain.exception.*;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;


public interface UserService {
    boolean isActive(User user);
    boolean hasLevel(User user, int level);
    boolean hasAuth(Authentication authentication, int level);
    UserLoginResponse login(UserLogin userLogin) throws BadRequestException, BadGatewayException;
    void signup(UserSignup userSignup);
    void createUser(UserSignup userSignup) throws NotFoundException, ConflictException, BadGatewayException;
    UserEntity getUserEntity(UUID uuid) throws NotFoundException;
    User getUser(UUID uuid);
    User getUserByExternalUuidAndCommunity(String externalUuid, String communityId) throws NotFoundException;
    List<UserTypeDTO> getAllUserTypesFilterByAuth(Authentication authentication);
    void updateStatusOfUser(String uuid, UserStatusDTO userStatusDTO) throws NotFoundException;
    void addTypeOfUser(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws NotFoundException, ForbiddenException;
    void deleteTypeOfUser(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws NotFoundException, ForbiddenException;
}

package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.request.SignupRequest;
import com.alfred.backoffice.modules.auth.application.dto.request.UserLogin;
import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.application.dto.response.*;
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
    SignupResponse signupUsers(Authentication authentication, SignupRequest signupRequest) throws ForbiddenException, NotFoundException, BadRequestException;
    UserEntity getUserEntity(UUID uuid) throws NotFoundException;
    User getUser(UUID uuid);
    User getUserByExternalUuidAndCommunity(String externalUuid, String communityId) throws NotFoundException, BadRequestException;
    User getUserByUuidAndExternalUuid(String uuid, String externalUuid) throws NotFoundException, BadRequestException;
    List<UserTypeDTO> getAllUserTypesFilterByAuth(Authentication authentication);
    UserDTO updateStatusOfUser(String uuid, UserStatusDTO userStatusDTO) throws NotFoundException, BadRequestException;
    UserDTO addTypeOfUser(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws NotFoundException, ForbiddenException, BadRequestException;
    UserDTO deleteTypeOfUser(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws NotFoundException, ForbiddenException, BadRequestException;
}

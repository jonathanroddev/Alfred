package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.CommunityMapper;
import com.alfred.backoffice.modules.auth.application.dto.mapper.UserMapper;
import com.alfred.backoffice.modules.auth.application.dto.request.UserLogin;
import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.application.dto.response.UserDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.UserLoginResponse;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.UserTypeDTO;
import com.alfred.backoffice.modules.auth.domain.exception.*;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.domain.model.UserType;
import com.alfred.backoffice.modules.auth.domain.repository.UserRepository;
import com.alfred.backoffice.modules.auth.domain.service.CommunityService;
import com.alfred.backoffice.modules.auth.domain.service.UserService;
import com.alfred.backoffice.modules.auth.domain.service.UserStatusService;
import com.alfred.backoffice.modules.auth.domain.service.UserTypeService;
import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.model.FirebaseSignInResponse;
import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.service.FirebaseService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.CommunityEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserStatusEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserTypeEntity;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FirebaseService firebaseService;
    private final CommunityMapper communityMapper;
    private final CommunityService communityService;
    private final UserStatusService userStatusService;
    private final UserTypeService userTypeService;

    @SneakyThrows
    public boolean isActive(User user) {
        return Objects.equals(user.getUserStatus().getName(), "active");
    }

    @SneakyThrows
    public boolean hasLevel(User user, int level) {
        return user.getUserTypes().stream().anyMatch(userType -> userType.getLevel() <= level);
    }

    @SneakyThrows
    public boolean hasAuth(Authentication authentication, int level) {
        return this.hasLevel((User) authentication.getPrincipal(), level);
    }

    @Override
    public UserLoginResponse login(UserLogin userLogin) throws BadRequestException, BadGatewayException {
        FirebaseSignInResponse firebaseSignInResponse = this.firebaseService.login(userLogin.getEmail(), userLogin.getPassword());
        List<UserDTO> users = this.userMapper.toDTOList(this.userRepository.findAllByExternalUuid(firebaseSignInResponse.getLocalId()));
        return new UserLoginResponse(firebaseSignInResponse, users);
    }

    // TODO: Do askForRegistration method. This method should send a mail to the admin. Target: new customers.

    @SneakyThrows
    @Override
    public UserDTO createUser(Authentication authentication, UserSignup userSignup) throws ForbiddenException {
        User manager = (User) authentication.getPrincipal();
        if (!this.isAdmin(manager) && (!Objects.equals(manager.getCommunity().getUuid(), userSignup.getCommunityId()))) {
            throw new ForbiddenException("amg-403_3");
        }
       return this.createUser(manager.getUuid(), userSignup);
    }

    private UserDTO createUser(String managerId, UserSignup userSignup) throws NotFoundException, ConflictException, BadGatewayException, BadRequestException {
        /* TODO: Check flow.
        The best scenario is not to send the password. That's because this process involves an admin or a manager
        instead of an user himself. One thing we can do, is auto generate a password and sent it to the final user
        besides a link to reset it. Also, a mail has to be sent to the admin in order to change the user status.
         */
        CommunityEntity communityEntity = this.communityService.getCommunityEntity(userSignup.getCommunityId());
        UserStatusEntity userStatusEntity = this.userStatusService.getUserStatusEntity("pending");
        String externalUuid = "";
        try {
            externalUuid = this.firebaseService.createUser(userSignup.getMail(), userSignup.getPassword());
        } catch (ConflictException ce) {
            externalUuid = ce.getCode();
            Optional<UserEntity> userEntity = this.userRepository.findByExternalUuidAndCommunityUuid(externalUuid, communityEntity.getUuid());
            if (userEntity.isPresent()) {
                throw new ConflictException("amg-409_1");
            }
        } catch (FirebaseAuthException fbe) {
            throw new BadGatewayException("amg-502_1");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setExternalUuid(externalUuid);
        userEntity.setCommunity(communityEntity);
        userEntity.setUserStatus(userStatusEntity);
        userEntity.setCreatedBy(managerId);
        userRepository.save(userEntity);
        return this.userMapper.toDTO(userEntity);
    }

    @Transactional
    @Override
    public UserEntity getUserEntity(UUID uuid) throws NotFoundException {
        Optional<UserEntity> userEntity =  userRepository.findById(uuid);
        if (userEntity.isPresent()){
            return userEntity.get();
        }
        throw new NotFoundException("amg-404_1");
    }

    @SneakyThrows
    @Override
    public User getUser(UUID uuid) {
        return userMapper.toModel(this.getUserEntity(uuid));
    }

    @Transactional
    @Override
    public User getUserByExternalUuidAndCommunity(String externalUuid, String communityId) throws NotFoundException, BadRequestException{
        try {
            Optional<UserEntity> userEntity = userRepository.findByExternalUuidAndCommunityUuid(externalUuid, UUID.fromString(communityId));
            if (userEntity.isPresent()){
                return userMapper.toModel(userEntity.get());
            }
            throw new NotFoundException("amg-404_1");
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException("amg-400_5");
        }
    }

    @Transactional
    @Override
    public User getUserByUuidAndExternalUuid(String uuid, String externalUuid) throws NotFoundException, BadRequestException {
        try {
            Optional<UserEntity> userEntity = userRepository.findByUuidAndExternalUuid(UUID.fromString(uuid), externalUuid);
            if (userEntity.isPresent()){
                return userMapper.toModel(userEntity.get());
            }
            throw new NotFoundException("amg-404_1");
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException("amg-400_6");
        }
    }

    @Override
    public List<UserTypeDTO> getAllUserTypesFilterByAuth(Authentication authentication) {
        return userTypeService.getAllUserTypesFilterByUser((User) authentication.getPrincipal());
    }

    private boolean isAdmin(User user) {
        return user.getUserTypes().stream().anyMatch(userType -> userType.getLevel() <= 0);
    }

    private boolean shareCommunity(User manager, User user) throws NotFoundException, BadRequestException{
        return Objects.equals(manager.getCommunity(), user.getCommunity());
    }

    private boolean isOverUser(User manager, User user) {
        UserType managerMinUserType = manager.getUserTypes().stream().min(Comparator.comparingInt(UserType::getLevel)).get();
        UserType userMinUserType = user.getUserTypes().stream().min(Comparator.comparingInt(UserType::getLevel)).get();
        return managerMinUserType.getLevel() < userMinUserType.getLevel();
    }

    private void canPerform(User manager, User user) throws NotFoundException, ForbiddenException, BadRequestException {
        if (!this.isAdmin(manager) && (!this.shareCommunity(manager, user) || !this.isOverUser(manager, user))) {
            throw new ForbiddenException("amg-403_1");
        }
    }

    @Transactional
    @Override
    public UserDTO updateStatusOfUser(String uuid, UserStatusDTO userStatusDTO) throws NotFoundException, BadRequestException {
        try {
            UserEntity userEntity = this.getUserEntity(UUID.fromString(uuid));
            UserStatusEntity userStatusEntity = this.userStatusService.getUserStatusEntity(userStatusDTO.getName());
            userEntity.setUserStatus(userStatusEntity);
            this.userRepository.save(userEntity);
            return this.userMapper.toDTO(userEntity);
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException("amg-400_6");
        }
    }

    private UserEntity getUserToPerformTypeUpdates(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws NotFoundException, ForbiddenException, BadRequestException {
        try {
            UserTypeEntity userTypeEntity = this.userTypeService.getUserTypeEntity(userTypeDTO.getName());
            UserEntity userEntity = this.getUserEntity(UUID.fromString(uuid));
            User user = userMapper.toModel(userEntity);
            User manager = (User) authentication.getPrincipal();
            // Check if manager can perform action over user
            this.canPerform(manager, user);
            // Check that the level of the user type to set is higher than the lowest of the manager
            UserType managerMinUserType = manager.getUserTypes().stream().min(Comparator.comparingInt(UserType::getLevel)).get();
            if (userTypeEntity.getLevel() <= managerMinUserType.getLevel()) {
                throw new ForbiddenException("amg-403_2");
            }
            return userEntity;
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException("amg-400_6");
        }
    }

    @Override
    public UserDTO addTypeOfUser(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws NotFoundException, ForbiddenException, BadRequestException {
        UserEntity userEntity = this.getUserToPerformTypeUpdates(authentication, uuid, userTypeDTO);
        User manager = (User) authentication.getPrincipal();
        userEntity.setUpdatedBy(manager.getUuid());
        Set<UserTypeEntity> userTypes = userEntity.getUserTypes();
        UserTypeEntity userTypeEntity = this.userTypeService.getUserTypeEntity(userTypeDTO.getName());
        if (!userTypes.contains(userTypeEntity)) {
            userTypes.add(userTypeEntity);
            userEntity.setUserTypes(userTypes);
            userRepository.save(userEntity);
        }
        return this.userMapper.toDTO(userEntity);
    }

    @Override
    public UserDTO deleteTypeOfUser(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws NotFoundException, ForbiddenException, BadRequestException {
        UserEntity userEntity = this.getUserToPerformTypeUpdates(authentication, uuid, userTypeDTO);
        User manager = (User) authentication.getPrincipal();
        userEntity.setUpdatedBy(manager.getUuid());
        Set<UserTypeEntity> userTypes = userEntity.getUserTypes();
        UserTypeEntity userTypeEntity = this.userTypeService.getUserTypeEntity(userTypeDTO.getName());
        if (userTypes.contains(userTypeEntity)) {
            userTypes.remove(userTypeEntity);
            userEntity.setUserTypes(userTypes);
            userRepository.save(userEntity);
        }
        return this.userMapper.toDTO(userEntity);
    }
}

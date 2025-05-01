package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.CommunityMapper;
import com.alfred.backoffice.modules.auth.application.dto.mapper.UserMapper;
import com.alfred.backoffice.modules.auth.application.dto.request.UserLogin;
import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.application.dto.response.UserLoginResponse;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.UserTypeDTO;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.domain.model.UserType;
import com.alfred.backoffice.modules.auth.domain.repository.UserRepository;
import com.alfred.backoffice.modules.auth.domain.repository.UserStatusRepository;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // TODO: Consider to call to CommunityService, UserStatusService instead of their mapper and repository

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FirebaseService firebaseService;
    private final CommunityMapper communityMapper;
    private final CommunityService communityService;
    private final UserStatusRepository userStatusRepository;
    private final UserStatusService userStatusService;
    private final UserTypeService userTypeService;

    // TODO: This must be migrate to FirebaseAuthenticationFilter
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
    public UserLoginResponse login(UserLogin userLogin) throws Exception {
        FirebaseSignInResponse firebaseSignInResponse = this.firebaseService.login(userLogin.getEmail(), userLogin.getPassword());
        List<User> users = this.userMapper.toModelList(this.userRepository.findAllByExternalUuid(firebaseSignInResponse.getLocalId()));
        return new UserLoginResponse(firebaseSignInResponse, users);
    }

    // Method used by Alfred's team
    @SneakyThrows
    @Override
    public void signup(UserSignup userSignup){
       this.createUser(userSignup);
    }

    // TODO: Create method for massive signup for managers. Example: List<String> mails | Getting the community by SecurityContextHolder

    @Override
    public void createUser(UserSignup userSignup) throws Exception {
        CommunityEntity communityEntity = this.communityService.getCommunityEntity(userSignup.getCommunityId());
        // TODO: Handle exception on duplicate in Firebase to check if exists with same community in Alfred
        String externalUuid = this.firebaseService.createUser(userSignup.getMail(), userSignup.getPassword());
        Optional<UserStatusEntity> userStatusEntity = userStatusRepository.findById("pending");
        if (userStatusEntity.isPresent()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setExternalUuid(externalUuid);
            userEntity.setCommunity(communityEntity);
            userEntity.setUserStatus(userStatusEntity.get());
            userRepository.save(userEntity);
        }
    }

    @Transactional
    @Override
    public UserEntity getUserEntity(UUID uuid) throws Exception {
        Optional<UserEntity> userEntity =  userRepository.findById(uuid);
        if (userEntity.isPresent()){
            return userEntity.get();
        }
        // TODO: Handle throw custom exception. Extend of RuntimeException
        throw new Exception();
    }

    @SneakyThrows
    @Override
    public User getUser(UUID uuid) {
        return userMapper.toModel(this.getUserEntity(uuid));
    }

    @Transactional
    @Override
    public User getUserByExternalUuidAndCommunity(String externalUuid, String communityId) throws Exception {
        // TODO: Handle exceptions on UUID
        Optional<UserEntity> userEntity = userRepository.findByExternalUuidAndCommunityUuid(externalUuid, UUID.fromString(communityId));
        if (userEntity.isPresent()){
            return userMapper.toModel(userEntity.get());
        }
        // TODO: Handle throw custom exception. Extend of RuntimeException
        throw new Exception();
    }

    @Override
    public List<UserTypeDTO> getAllUserTypesFilterByAuth(Authentication authentication) throws Exception {
        return userTypeService.getAllUserTypesFilterByUser((User) authentication.getPrincipal());
    }

    private boolean isAdmin(User user) {
        return user.getUserTypes().stream().anyMatch(userType -> userType.getLevel() <= 0);
    }

    private boolean shareCommunity(User manager, User user) {
        return Objects.equals(manager.getCommunity(), user.getCommunity());
    }

    private boolean isOverUser(User manager, User user) {
        UserType managerMinUserType = manager.getUserTypes().stream().min(Comparator.comparingInt(UserType::getLevel)).get();
        UserType userMinUserType = user.getUserTypes().stream().min(Comparator.comparingInt(UserType::getLevel)).get();
        return managerMinUserType.getLevel() < userMinUserType.getLevel();
    }

    private void canPerform(User manager, User user) throws Exception {
        if (!this.isAdmin(manager) && (!this.shareCommunity(manager, user) || !this.isOverUser(manager, user))) {
            // TODO: Handle throw custom exception. Extend of RuntimeException
            throw new Exception();
        }
    }

    @Transactional
    @Override
    public void updateStatusOfUser(String uuid, UserStatusDTO userStatusDTO) throws Exception {
        // TODO: Handle UUID.fromString exceptions in all project
        UserEntity userEntity = this.getUserEntity(UUID.fromString(uuid));
        UserStatusEntity userStatusEntity = this.userStatusService.getUserStatusEntity(userStatusDTO.getName());
        userEntity.setUserStatus(userStatusEntity);
        this.userRepository.save(userEntity);
    }

    private UserEntity getUserToPerformTypeUpdates(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws Exception {
        UserTypeEntity userTypeEntity = this.userTypeService.getUserTypeEntity(userTypeDTO.getName());
        UserEntity userEntity = this.getUserEntity(UUID.fromString(uuid));
        User user = userMapper.toModel(userEntity);
        User manager = (User) authentication.getPrincipal();
        // Check if manager can perform action over user
        this.canPerform(manager, user);
        // Check that the level of the user type to set is higher than the lowest of the manager
        UserType managerMinUserType = manager.getUserTypes().stream().min(Comparator.comparingInt(UserType::getLevel)).get();
        if (userTypeEntity.getLevel() <= managerMinUserType.getLevel()) {
            // TODO: Handle throw custom exception. Extend of RuntimeException
            throw new Exception();
        }
        return userEntity;
    }

    @Override
    public void addTypeOfUser(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws Exception {
        UserEntity userEntity = this.getUserToPerformTypeUpdates(authentication, uuid, userTypeDTO);
        Set<UserTypeEntity> userTypes = userEntity.getUserTypes();
        UserTypeEntity userTypeEntity = this.userTypeService.getUserTypeEntity(userTypeDTO.getName());
        if (!userTypes.contains(userTypeEntity)) {
            userTypes.add(userTypeEntity);
            userEntity.setUserTypes(userTypes);
            userRepository.save(userEntity);
        }
    }

    @Override
    public void deleteTypeOfUser(Authentication authentication, String uuid, UserTypeDTO userTypeDTO) throws Exception {
        UserEntity userEntity = this.getUserToPerformTypeUpdates(authentication, uuid, userTypeDTO);
        Set<UserTypeEntity> userTypes = userEntity.getUserTypes();
        UserTypeEntity userTypeEntity = this.userTypeService.getUserTypeEntity(userTypeDTO.getName());
        if (userTypes.contains(userTypeEntity)) {
            userTypes.remove(userTypeEntity);
            userEntity.setUserTypes(userTypes);
            userRepository.save(userEntity);
        }
    }
}

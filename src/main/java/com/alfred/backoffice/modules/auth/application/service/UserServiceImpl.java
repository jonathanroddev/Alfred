package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.CommunityMapper;
import com.alfred.backoffice.modules.auth.application.dto.mapper.UserMapper;
import com.alfred.backoffice.modules.auth.application.dto.mapper.UserStatusMapper;
import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.domain.model.UserStatus;
import com.alfred.backoffice.modules.auth.domain.repository.UserRepository;
import com.alfred.backoffice.modules.auth.domain.repository.UserStatusRepository;
import com.alfred.backoffice.modules.auth.domain.service.CommunityService;
import com.alfred.backoffice.modules.auth.domain.service.UserService;
import com.alfred.backoffice.modules.auth.domain.service.UserStatusService;
import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.service.FirebaseService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.CommunityEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserStatusEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
    public User getUser(String externalUuid) throws Exception {
        Optional<UserEntity> userEntity = userRepository.findByExternalUuid(externalUuid);
        if (userEntity.isPresent()){
            return userMapper.toModel(userEntity.get());
        }
        throw new Exception();
    }

    @SneakyThrows
    @Transactional
    @Override
    public void updateStatusOfUser(String uuid, UserStatusDTO userStatusDTO) {
        // TODO: Handle UUID.fromString exceptions in all project
        UserEntity userEntity = this.getUserEntity(UUID.fromString(uuid));
        UserStatusEntity userStatusEntity = this.userStatusService.getUserStatusEntity(userStatusDTO.getName());
        userEntity.setUserStatus(userStatusEntity);
        this.userRepository.save(userEntity);
    }
}

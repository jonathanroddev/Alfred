package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.CommunityMapper;
import com.alfred.backoffice.modules.auth.application.dto.mapper.UserMapper;
import com.alfred.backoffice.modules.auth.application.request.UserSignup;
import com.alfred.backoffice.modules.auth.domain.model.Community;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.domain.repository.UserRepository;
import com.alfred.backoffice.modules.auth.domain.repository.UserStatusRepository;
import com.alfred.backoffice.modules.auth.domain.service.UserService;
import com.alfred.backoffice.modules.auth.infrastructure.external.firebase.service.FirebaseService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserStatusEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // TODO: Call to CommunityService and UserStatusService instead of their mapper and repository

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FirebaseService firebaseService;
    private final CommunityMapper communityMapper;
    private final UserStatusRepository userStatusRepository;

    // Method used by Alfred's team
    @Override
    public void signup(UserSignup userSignup) throws Exception {
       this.createUser(userSignup.getMail(), this.communityMapper.toModel(userSignup.getCommunityDTO()));
    }

    // TODO: Create method for massive signup for managers. Example: List<String> mails | Getting the community by SecurityContextHolder

    @Override
    public void createUser(String mail, Community community) throws Exception {
        // TODO: Pass password as argument
        String externalUuid = this.firebaseService.createUser(mail, "test12");
        Optional<UserStatusEntity> userStatusEntity = userStatusRepository.findById("pending");
        if (userStatusEntity.isPresent()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setExternalUuid(externalUuid);
            userEntity.setCommunity(communityMapper.toEntity(community));
            userEntity.setUserStatus(userStatusEntity.get());
            userRepository.save(userEntity);
        }
    }

    @Override
    public User getUser(UUID uuid) throws Exception {
        Optional<UserEntity> userEntity = userRepository.findById(uuid);
        if (userEntity.isPresent()){
            return userMapper.toModel(userEntity.get());
        }
        throw new Exception();
    }

    @Override
    public User getUser(String externalUuid) throws Exception {
        Optional<UserEntity> userEntity = userRepository.findByExternalUuid(externalUuid);
        if (userEntity.isPresent()){
            return userMapper.toModel(userEntity.get());
        }
        throw new Exception();
    }
}

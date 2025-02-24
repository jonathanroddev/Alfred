package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.CommunityMapper;
import com.alfred.backoffice.modules.auth.application.request.UserSignup;
import com.alfred.backoffice.modules.auth.domain.model.Community;
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

    private final UserRepository userRepository;
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
        // TODO: Add automatic password generation in order to send a mail to the user so they can reset it
        String userUuid = this.firebaseService.createUser(mail, "test12");
        Optional<UserStatusEntity> userStatusEntity = userStatusRepository.findById("pending");
        if (userStatusEntity.isPresent()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUuid(UUID.fromString(userUuid));
            userEntity.setCommunity(communityMapper.toEntity(community));
            userEntity.setUserStatus(userStatusEntity.get());
            userRepository.save(userEntity);
        }
    }
}

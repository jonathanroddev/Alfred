package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.UserStatusMapper;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.domain.repository.UserStatusRepository;
import com.alfred.backoffice.modules.auth.domain.service.UserStatusService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserStatusEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserStatusMapper userStatusMapper;

    @Override
    public List<UserStatusDTO> getAllUserStatus() {
        List<UserStatusEntity> userStatusEntities = userStatusRepository.findAll();
        return userStatusMapper.toDTOList(userStatusEntities);
    }

    @Override
    public UserStatusEntity getUserStatusEntity(String name) throws Exception {
        Optional<UserStatusEntity> userStatusEntity = userStatusRepository.findByName(name);
        if (userStatusEntity.isPresent()){
            return userStatusEntity.get();
        }
        // TODO: Throw custom exception. Extend of RuntimeException
        throw new Exception();
    }
}

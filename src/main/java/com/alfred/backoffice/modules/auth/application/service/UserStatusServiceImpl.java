package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.UserStatusMapper;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.domain.exception.NotFoundException;
import com.alfred.backoffice.modules.auth.domain.repository.UserStatusRepository;
import com.alfred.backoffice.modules.auth.domain.service.UserStatusService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserStatusEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public UserStatusEntity getUserStatusEntity(String name) throws NotFoundException {
        Optional<UserStatusEntity> userStatusEntity = userStatusRepository.findById(name);
        if (userStatusEntity.isPresent()){
            return userStatusEntity.get();
        }
        throw new NotFoundException("amg-404_6");
    }
}

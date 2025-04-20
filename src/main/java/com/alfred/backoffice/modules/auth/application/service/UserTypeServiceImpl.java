package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.UserStatusMapper;
import com.alfred.backoffice.modules.auth.application.dto.mapper.UserTypeMapper;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.UserTypeDTO;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.domain.model.UserType;
import com.alfred.backoffice.modules.auth.domain.repository.UserStatusRepository;
import com.alfred.backoffice.modules.auth.domain.repository.UserTypeRepository;
import com.alfred.backoffice.modules.auth.domain.service.UserStatusService;
import com.alfred.backoffice.modules.auth.domain.service.UserTypeService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserStatusEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserTypeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeRepository userTypeRepository;
    private final UserTypeMapper userTypeMapper;

    @Override
    public List<UserTypeDTO> getAllUserTypes() {
        List<UserTypeEntity> userStatusEntities = userTypeRepository.findAll();
        return userTypeMapper.toDTOList(userStatusEntities);
    }

    @Override
    public List<UserTypeDTO> getAllUserTypesFilterByUser(User user) throws Exception {
        UserType userMinUserType = user.getUserTypes().stream().min(Comparator.comparingInt(UserType::getLevel)).get();
        List<UserTypeEntity> userStatusEntities = userTypeRepository.findByLevelGreaterThanEqual(userMinUserType.getLevel());
        return userTypeMapper.toDTOList(userStatusEntities);
    }

    @Override
    public UserTypeEntity getUserTypeEntity(String name) throws Exception {
        Optional<UserTypeEntity> userTypeEntity = userTypeRepository.findById(name);
        if (userTypeEntity.isPresent()){
            return userTypeEntity.get();
        }
        // TODO: Throw custom exception. Extend of RuntimeException
        throw new Exception();
    }
}

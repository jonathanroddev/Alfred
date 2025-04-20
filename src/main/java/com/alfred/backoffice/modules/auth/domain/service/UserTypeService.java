package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.response.UserTypeDTO;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserTypeEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserTypeService {
    List<UserTypeDTO> getAllUserTypes();
    List<UserTypeDTO> getAllUserTypesFilterByUser(User user) throws Exception;
    UserTypeEntity getUserTypeEntity(String name) throws Exception;
}

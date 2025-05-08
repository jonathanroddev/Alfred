package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.domain.exception.NotFoundException;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserStatusEntity;

import java.util.List;

public interface UserStatusService {
    List<UserStatusDTO> getAllUserStatus();
    UserStatusEntity getUserStatusEntity(String name) throws NotFoundException;
}

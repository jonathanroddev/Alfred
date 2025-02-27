package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.domain.model.UserStatus;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserStatusEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserStatusMapper {
    UserStatus toModel(UserStatusEntity userStatusEntity);
}

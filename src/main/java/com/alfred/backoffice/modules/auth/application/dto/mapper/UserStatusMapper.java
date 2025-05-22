package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.domain.model.UserStatus;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserStatusEntity;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserStatusMapper {
    UserStatus toModel(UserStatusEntity userStatusEntity);
    UserStatus toModel(UserStatusDTO userStatusDTO);
    List<UserStatusDTO> toDTOList(List<UserStatusEntity> userStatusEntityList);
    UserStatusEntity toEntity(UserStatus userStatus);
}

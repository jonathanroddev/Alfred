package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.application.dto.response.UserTypeDTO;
import com.alfred.backoffice.modules.auth.domain.model.UserType;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserTypeEntity;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserTypeMapper {
    UserType toModel(UserTypeEntity userTypeEntity);
    List<UserTypeDTO> toDTOList(List<UserTypeEntity> userTypeEntityList);
}

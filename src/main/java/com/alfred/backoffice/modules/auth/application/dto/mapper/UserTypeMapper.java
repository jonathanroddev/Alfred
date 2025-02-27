package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.domain.model.UserType;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserTypeEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserTypeMapper {
    UserType toModel(UserTypeEntity userTypeEntity);
}

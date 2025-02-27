package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring", uses = { UserStatusMapper.class, CommunityMapper.class, RoleMapper.class, UserTypeMapper.class })
public interface UserMapper {
    User toModel(UserEntity userEntity);
}

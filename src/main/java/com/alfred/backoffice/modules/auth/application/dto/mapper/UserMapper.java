package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.application.dto.response.UserDTO;
import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring", uses = { UserStatusMapper.class, CommunityMapper.class, RoleMapper.class, UserTypeMapper.class })
public interface UserMapper {
    User toModel(UserEntity userEntity);
    UserEntity toEntity(User user);
    List<User> toModelList(List<UserEntity> userEntities);
    List<UserDTO> toDTOList(List<UserEntity> userEntities);
    UserDTO toDTO(UserEntity userEntity);
}

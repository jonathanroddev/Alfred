package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.domain.model.Role;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.RoleEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = CommunityMapper.class)
public interface RoleMapper {
    Role toModel(RoleEntity roleEntity);
}

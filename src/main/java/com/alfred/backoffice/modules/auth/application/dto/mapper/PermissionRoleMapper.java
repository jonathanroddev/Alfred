package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.domain.model.PermissionRole;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.PermissionRoleEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {PermissionMapper.class, RoleMapper.class})
public interface PermissionRoleMapper {
    PermissionRole toModel(PermissionRoleEntity permissionRoleEntity);
}

package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.domain.model.Permission;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.PermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = OperationMapper.class)
public interface PermissionMapper {
    @Mapping(target = "operation", source = "operation")
    Permission toModel(PermissionEntity permissionEntity);
}

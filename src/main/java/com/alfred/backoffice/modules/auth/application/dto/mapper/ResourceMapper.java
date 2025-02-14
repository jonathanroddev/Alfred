package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.application.dto.response.ResourceDTO;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.ResourceEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
    ResourceDTO toDTO(ResourceEntity resourceEntity);
    List<ResourceDTO> toDTOList(List<ResourceEntity> resourceEntityList);
}

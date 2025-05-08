package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.response.ResourceDTO;
import com.alfred.backoffice.modules.auth.domain.exception.NotFoundException;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.ResourceEntity;

import java.util.List;

public interface ResourceService {
    List<ResourceDTO> getAllResources();
    ResourceEntity getResourceEntity(String name) throws NotFoundException;
    ResourceDTO createResource(ResourceDTO resourceDTO) throws Exception;
}

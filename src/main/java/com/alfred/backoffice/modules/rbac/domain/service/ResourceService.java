package com.alfred.backoffice.modules.rbac.domain.service;

import com.alfred.backoffice.modules.rbac.application.dto.response.ResourceDTO;

import java.util.List;

public interface ResourceService {
    List<ResourceDTO> getAllResources();
}

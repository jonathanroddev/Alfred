package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.response.ResourceDTO;

import java.util.List;

public interface ResourceService {
    List<ResourceDTO> getAllResources();
}

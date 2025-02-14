package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.ResourceMapper;
import com.alfred.backoffice.modules.auth.application.dto.response.ResourceDTO;
import com.alfred.backoffice.modules.auth.domain.repository.ResourceRepository;
import com.alfred.backoffice.modules.auth.domain.service.ResourceService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.ResourceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    @Override
    public List<ResourceDTO> getAllResources() {
        List<ResourceEntity> resourceEntities = resourceRepository.findAll();
        return resourceMapper.toDTOList(resourceEntities);
    }
}

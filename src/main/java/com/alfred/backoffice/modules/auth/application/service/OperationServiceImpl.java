package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.OperationMapper;
import com.alfred.backoffice.modules.auth.application.dto.response.OperationDTO;
import com.alfred.backoffice.modules.auth.domain.repository.OperationRepository;
import com.alfred.backoffice.modules.auth.domain.service.OperationService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.OperationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;

    @Override
    public List<OperationDTO> getAllOperations() {
        List<OperationEntity> operationEntities = operationRepository.findAll();
        return operationMapper.toDTOList(operationEntities);
    }
}

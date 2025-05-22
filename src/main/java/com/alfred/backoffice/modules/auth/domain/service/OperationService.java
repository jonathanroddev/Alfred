package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.response.OperationDTO;

import java.util.List;

public interface OperationService {
    List<OperationDTO> getAllOperations();
}

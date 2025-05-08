package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.response.PlanDTO;
import com.alfred.backoffice.modules.auth.domain.exception.NotFoundException;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.PlanEntity;

import java.util.List;

public interface PlanService {
    List<PlanDTO> getAllPlans();
    PlanEntity getPlanEntity(String name) throws NotFoundException;
    PlanDTO createPlan(PlanDTO planDTO) throws NotFoundException;
}

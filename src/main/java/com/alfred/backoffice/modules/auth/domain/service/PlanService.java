package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.response.PlanDTO;

import java.util.List;

public interface PlanService {
    List<PlanDTO> getAllPlans();
}

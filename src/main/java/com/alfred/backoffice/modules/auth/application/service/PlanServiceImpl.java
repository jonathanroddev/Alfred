package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.PlanMapper;
import com.alfred.backoffice.modules.auth.application.dto.response.PlanDTO;
import com.alfred.backoffice.modules.auth.domain.repository.PlanRepository;
import com.alfred.backoffice.modules.auth.domain.service.PlanService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.PlanEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    @Override
    public List<PlanDTO> getAllPlans() {
        List<PlanEntity> planEntities = planRepository.findAll();
        return planMapper.toDTOList(planEntities);
    }

    @Transactional
    @Override
    public PlanEntity getPlanEntity(String name) throws Exception {
        Optional<PlanEntity> planEntity =  planRepository.findById(name);
        if (planEntity.isPresent()){
            return planEntity.get();
        }
        // TODO: Handle throw custom exception
        throw new Exception();
    }

}

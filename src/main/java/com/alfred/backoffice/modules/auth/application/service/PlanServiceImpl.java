package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.PlanMapper;
import com.alfred.backoffice.modules.auth.application.dto.response.PlanDTO;
import com.alfred.backoffice.modules.auth.domain.repository.PlanRepository;
import com.alfred.backoffice.modules.auth.domain.service.PlanService;
import com.alfred.backoffice.modules.auth.domain.service.ResourceService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.PlanEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.ResourceEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;
    private final ResourceService resourceService;

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
        // TODO: Handle throw custom exception. Extend of RuntimeException
        throw new Exception();
    }

    @Override
    public PlanDTO createPlan(PlanDTO planDTO) throws Exception {
        // TODO: Handle at least one resource
        Set<ResourceEntity> resourceEntities = planDTO.getResources().stream().map(resourceDTO -> {
            try {
                return this.resourceService.getResourceEntity(resourceDTO.getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toSet());
        PlanEntity planEntity = new PlanEntity(planDTO.getName(), resourceEntities);
        this.planRepository.save(planEntity);
        return this.planMapper.toDTO(planEntity);
    }

}

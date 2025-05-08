package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.PlanMapper;
import com.alfred.backoffice.modules.auth.application.dto.response.PlanDTO;
import com.alfred.backoffice.modules.auth.domain.exception.NotFoundException;
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
    public PlanEntity getPlanEntity(String name) throws NotFoundException {
        Optional<PlanEntity> planEntity =  planRepository.findById(name);
        if (planEntity.isPresent()){
            return planEntity.get();
        }
        throw new NotFoundException("amg-404_5");
    }

    @Override
    public PlanDTO createPlan(PlanDTO planDTO) throws NotFoundException {
        // TODO: Handle at least one resource
        Set<ResourceEntity> resourceEntities = planDTO.getResources().stream().map(resourceDTO -> {
            return this.resourceService.getResourceEntity(resourceDTO.getName());
        }).collect(Collectors.toSet());
        PlanEntity planEntity = new PlanEntity(planDTO.getName(), resourceEntities);
        this.planRepository.save(planEntity);
        return this.planMapper.toDTO(planEntity);
    }

}

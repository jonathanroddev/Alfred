package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.application.dto.response.PlanDTO;
import com.alfred.backoffice.modules.auth.domain.model.Plan;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.PlanEntity;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring", uses = ResourceMapper.class)
public interface PlanMapper {
    Plan toModel(PlanEntity planEntity);
    List<PlanDTO> toDTOList(List<PlanEntity> planEntityList);
    PlanDTO toDTO(PlanEntity planEntity);
}

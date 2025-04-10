package com.alfred.backoffice.modules.auth.application.dto.mapper;

import com.alfred.backoffice.modules.auth.application.dto.response.CommunityDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.PlanDTO;
import com.alfred.backoffice.modules.auth.domain.model.Community;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.CommunityEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.PlanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring", uses = PlanMapper.class)
public interface CommunityMapper {
    Community toModel(CommunityEntity communityEntity);

    @Mapping(target = "plan", source = "plan")
    Community toModel(CommunityDTO communityDTO);

    CommunityEntity toEntity(Community community);

    CommunityEntity toEntity(CommunityDTO communityDTO);

    @Mapping(target = "plan", source = "plan")
    List<CommunityDTO> toDTOList(List<CommunityEntity> communityEntityList);

    @Mapping(target = "plan", source = "plan")
    CommunityDTO toDTO(CommunityEntity communityEntity);

}

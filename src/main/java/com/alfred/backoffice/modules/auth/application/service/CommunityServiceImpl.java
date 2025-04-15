package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.CommunityMapper;
import com.alfred.backoffice.modules.auth.application.dto.mapper.PlanMapper;
import com.alfred.backoffice.modules.auth.application.dto.response.CommunityDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.PlanDTO;
import com.alfred.backoffice.modules.auth.domain.model.Community;
import com.alfred.backoffice.modules.auth.domain.repository.CommunityRepository;
import com.alfred.backoffice.modules.auth.domain.repository.PlanRepository;
import com.alfred.backoffice.modules.auth.domain.service.CommunityService;
import com.alfred.backoffice.modules.auth.domain.service.PlanService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.CommunityEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.PlanEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final PlanService planService;

    @Override
    public List<CommunityDTO> getAllCommunities() {
        List<CommunityEntity> communityEntityList = this.communityRepository.findAll();
        return communityMapper.toDTOList(communityEntityList);
    }

    @SneakyThrows
    @Override
    public Community getCommunity(String uuid) {
        return communityMapper.toModel(this.getCommunityEntity(uuid));
    }

    @Override
    public CommunityEntity getCommunityEntity(String uuid) throws Exception {
        Optional<CommunityEntity> communityEntity = this.communityRepository.findById(UUID.fromString(uuid));
        if (communityEntity.isEmpty()) {
            // TODO: Throw custom exception. Extend of RuntimeException
            throw new Exception();
        }
        return communityEntity.get();
    }

    @Override
    public CommunityDTO createCommunity(CommunityDTO communityDTO) throws Exception {
        // TODO: Handle nullpointer on plan's name
        PlanEntity planEntity = this.planService.getPlanEntity(communityDTO.getPlan().getName());
        CommunityEntity communityEntity = this.communityMapper.toEntity(communityDTO);
        communityEntity.setUuid(UUID.randomUUID());
        communityEntity.setPlan(planEntity);
        this.communityRepository.save(communityEntity);
        return this.communityMapper.toDTO(communityEntity);
    }
}

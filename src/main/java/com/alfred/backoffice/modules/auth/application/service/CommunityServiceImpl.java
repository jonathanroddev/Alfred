package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.application.dto.mapper.CommunityMapper;
import com.alfred.backoffice.modules.auth.application.dto.response.CommunityDTO;
import com.alfred.backoffice.modules.auth.domain.exception.NotFoundException;
import com.alfred.backoffice.modules.auth.domain.exception.UnprocessableEntityException;
import com.alfred.backoffice.modules.auth.domain.model.Community;
import com.alfred.backoffice.modules.auth.domain.repository.CommunityRepository;
import com.alfred.backoffice.modules.auth.domain.service.CommunityService;
import com.alfred.backoffice.modules.auth.domain.service.PlanService;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.CommunityEntity;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.PlanEntity;
import lombok.RequiredArgsConstructor;
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

    @Override
    public Community getCommunity(String uuid) throws NotFoundException {
        return communityMapper.toModel(this.getCommunityEntity(uuid));
    }

    @Override
    public CommunityEntity getCommunityEntity(String uuid) throws NotFoundException {
        Optional<CommunityEntity> communityEntity = this.communityRepository.findById(UUID.fromString(uuid));
        if (communityEntity.isEmpty()) {
            throw new NotFoundException("amg-404_3");
        }
        return communityEntity.get();
    }

    @Override
    public CommunityDTO createCommunity(CommunityDTO communityDTO) throws NotFoundException, UnprocessableEntityException {
        Optional<CommunityEntity> oldCommunityEntity = this.communityRepository.findByName(communityDTO.getName());
        if (oldCommunityEntity.isPresent()) {
            throw new UnprocessableEntityException("amg-422_1");
        }
        PlanEntity planEntity = this.planService.getPlanEntity(communityDTO.getPlan().getName());
        CommunityEntity communityEntity = this.communityMapper.toEntity(communityDTO);
        communityEntity.setUuid(UUID.randomUUID());
        communityEntity.setPlan(planEntity);
        this.communityRepository.save(communityEntity);
        return this.communityMapper.toDTO(communityEntity);
    }
}

package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.response.CommunityDTO;
import com.alfred.backoffice.modules.auth.domain.model.Community;
import com.alfred.backoffice.modules.auth.infrastructure.persistence.CommunityEntity;

import java.util.List;

public interface CommunityService {
    List<CommunityDTO> getAllCommunities();
    Community getCommunity(String uuid);
    CommunityEntity getCommunityEntity(String uuid) throws Exception;
}

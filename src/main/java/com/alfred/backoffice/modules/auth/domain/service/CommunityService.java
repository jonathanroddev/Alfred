package com.alfred.backoffice.modules.auth.domain.service;

import com.alfred.backoffice.modules.auth.application.dto.response.CommunityDTO;

import java.util.List;

public interface CommunityService {
    List<CommunityDTO> getAllCommunities();
}

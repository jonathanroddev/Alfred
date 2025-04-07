package com.alfred.backoffice.modules.auth.application.controller;

import com.alfred.backoffice.modules.auth.application.dto.response.*;
import com.alfred.backoffice.modules.auth.domain.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO: use @RestControllerAdvice

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.v1.path}")
public class GetAuthController {

    private final PlanService planService;
    private final ResourceService resourceService;
    private final UserStatusService userStatusService;
    private final CommunityService communityService;
    private final OperationService operationService;

    @GetMapping(path = "${public.path}/plans")
    List<PlanDTO> getPlans() {
        return planService.getAllPlans();
    }

    @GetMapping(path = "${public.path}/resources")
    List<ResourceDTO> getResources() {
        return resourceService.getAllResources();
    }

    @GetMapping(path = "${public.path}/user-status")
    List<UserStatusDTO> getUserStatus() {
        return userStatusService.getAllUserStatus();
    }

    @GetMapping(path = "/communities")
    @PreAuthorize("@authorizationService.hasLevel(authentication, 0)")
    List<CommunityDTO> getCommunities() {
        return communityService.getAllCommunities();
    }

    @GetMapping(path = "/operations")
    @PreAuthorize("@authorizationService.hasLevel(authentication, 1)")
    List<OperationDTO> getOperations() {
        return operationService.getAllOperations();
    }
}

package com.alfred.backoffice.modules.auth.application.controller;

import com.alfred.backoffice.modules.auth.application.dto.response.*;
import com.alfred.backoffice.modules.auth.domain.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserTypeService userTypeService;
    private final UserService userService;

    @GetMapping(path = "${public.path}/plans")
    @Tag(name = "Plans")
    List<PlanDTO> getPlans() {
        return planService.getAllPlans();
    }

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 0)")
    @GetMapping(path = "/user-status")
    @Tag(name = "User")
    List<UserStatusDTO> getUserStatus() {
        return userStatusService.getAllUserStatus();
    }

    @GetMapping(path = "${public.path}/resources")
    @Tag(name = "Resources")
    List<ResourceDTO> getResources() {
        return resourceService.getAllResources();
    }

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 1)")
    @GetMapping(path = "/user-types")
    @Tag(name = "User")
    List<UserTypeDTO> getUserTypes() throws Exception {
        return userService.getAllUserTypesFilterByAuth(SecurityContextHolder.getContext().getAuthentication());
    }

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 0)")
    @GetMapping(path = "/communities")
    @Tag(name = "Communities")
    List<CommunityDTO> getCommunities() {
        return communityService.getAllCommunities();
    }

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 1)")
    @GetMapping(path = "/operations")
    @Tag(name = "Operations")
    List<OperationDTO> getOperations() {
        return operationService.getAllOperations();
    }
}

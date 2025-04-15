package com.alfred.backoffice.modules.auth.application.controller;

import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.application.dto.response.CommunityDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.PlanDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.ResourceDTO;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.domain.service.CommunityService;
import com.alfred.backoffice.modules.auth.domain.service.PlanService;
import com.alfred.backoffice.modules.auth.domain.service.ResourceService;
import com.alfred.backoffice.modules.auth.domain.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



// TODO: use @RestControllerAdvice

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.v1.path}")
public class WriteAuthController {

    private final UserService userService;
    private final CommunityService communityService;
    private final PlanService planService;
    private final ResourceService resourceService;

    @PostMapping(path = "/signup")
    @Tag(name = "Access")
    void createUser(@RequestBody UserSignup userSignup) {
        this.userService.signup(userSignup);
    }

    @PreAuthorize("@authorizationService.hasLevel(authentication, 0)")
    @PutMapping(path = "/user/{userId}/user-status")
    @Tag(name = "User")
    void updateUserStatus(@RequestBody UserStatusDTO userStatusDTO, @PathVariable String userId) {
        // TODO: Consider return something
        this.userService.updateStatusOfUser(userId, userStatusDTO);
    }

    @PreAuthorize("@authorizationService.hasLevel(authentication, 0)")
    @PostMapping(path = "/communities")
    @Tag(name = "Communities")
    CommunityDTO createCommunity(@RequestBody CommunityDTO communityDTO) throws Exception {
        return this.communityService.createCommunity(communityDTO);
    }

    @PreAuthorize("@authorizationService.hasLevel(authentication, 0)")
    @PostMapping(path = "/plans")
    @Tag(name = "Plans")
    PlanDTO createPlan(@RequestBody PlanDTO planDTO) throws Exception {
        return this.planService.createPlan(planDTO);
    }

    @PreAuthorize("@authorizationService.hasLevel(authentication, 0)")
    @PostMapping(path = "/resources")
    @Tag(name = "Resources")
    ResourceDTO createResource(@RequestBody ResourceDTO resourceDTO) throws Exception {
        return this.resourceService.createResource(resourceDTO);
    }

}

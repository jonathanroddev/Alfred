package com.alfred.backoffice.modules.auth.application.controller;

import com.alfred.backoffice.modules.auth.application.dto.request.Registry;
import com.alfred.backoffice.modules.auth.application.dto.request.SignupRequest;
import com.alfred.backoffice.modules.auth.application.dto.request.UserLogin;
import com.alfred.backoffice.modules.auth.application.dto.response.*;
import com.alfred.backoffice.modules.auth.domain.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.v1.path}")
public class WriteAuthController {

    private final UserService userService;
    private final CommunityService communityService;
    private final PlanService planService;
    private final ResourceService resourceService;
    private final CustomerService customerService;

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 1)")
    @PostMapping(path = "/users")
    @Tag(name = "Users")
    SignupResponse signupUsers(@RequestBody @Valid SignupRequest signupRequest) {
        return this.userService.signupUsers(SecurityContextHolder.getContext().getAuthentication(), signupRequest);
    }

    @PostMapping(path = "${public.path}/join")
    @Tag(name = "Customers")
    void askJoin(@RequestBody @Valid Registry registry) throws Exception {
        customerService.askJoin(registry);
    }

    @PostMapping(path = "/login")
    @Tag(name = "Access")
    UserLoginResponse login(@RequestBody @Valid UserLogin userLogin) throws Exception {
        return this.userService.login(userLogin);
    }

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 0)")
    @PatchMapping(path = "/users/{userId}/status")
    @Tag(name = "Users")
    UserDTO updateStatusOfUser(@RequestBody UserStatusDTO userStatusDTO, @PathVariable String userId) throws Exception {
        return this.userService.updateStatusOfUser(userId, userStatusDTO);
    }

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 1)")
    @PostMapping(path = "/users/{userId}/types")
    @Tag(name = "Users")
    UserDTO addTypeOfUser(@RequestBody UserTypeDTO userTypeDTO, @PathVariable String userId) throws Exception {
        return this.userService.addTypeOfUser(SecurityContextHolder.getContext().getAuthentication(), userId, userTypeDTO);
    }

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 1)")
    @DeleteMapping(path = "/users/{userId}/types")
    @Tag(name = "Users")
    UserDTO deleteTypeOfUser(@RequestBody UserTypeDTO userTypeDTO, @PathVariable String userId) throws Exception {
        return this.userService.deleteTypeOfUser(SecurityContextHolder.getContext().getAuthentication(), userId, userTypeDTO);
    }

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 0)")
    @PostMapping(path = "/communities")
    @Tag(name = "Communities")
    CommunityDTO createCommunity(@RequestBody @Valid CommunityDTO communityDTO) throws Exception {
        return this.communityService.createCommunity(communityDTO);
    }

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 0)")
    @PostMapping(path = "/plans")
    @Tag(name = "Plans")
    PlanDTO createPlan(@RequestBody @Valid PlanDTO planDTO) throws Exception {
        return this.planService.createPlan(planDTO);
    }

    @PreAuthorize("@userServiceImpl.hasAuth(authentication, 0)")
    @PostMapping(path = "/resources")
    @Tag(name = "Resources")
    ResourceDTO createResource(@RequestBody ResourceDTO resourceDTO) throws Exception {
        return this.resourceService.createResource(resourceDTO);
    }

}

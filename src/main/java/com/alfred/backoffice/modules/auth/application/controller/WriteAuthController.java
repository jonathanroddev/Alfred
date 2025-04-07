package com.alfred.backoffice.modules.auth.application.controller;

import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.application.dto.response.UserStatusDTO;
import com.alfred.backoffice.modules.auth.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



// TODO: use @RestControllerAdvice

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.v1.path}")
public class WriteAuthController {

    private final UserService userService;

    @PostMapping(path = "/signup")
    void createUser(@RequestBody UserSignup userSignup) {
        this.userService.signup(userSignup);
    }

    @PreAuthorize("@authorizationService.hasLevel(authentication, 0)")
    @PutMapping(path = "/user/{userId}/user-status")
    void updateUserStatus(@RequestBody UserStatusDTO userStatusDTO, @PathVariable String userId) {
        // TODO: Consider return something
        this.userService.updateStatusOfUser(userId, userStatusDTO);
    }

}

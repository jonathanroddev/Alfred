package com.alfred.backoffice.modules.auth.application.controller;

import com.alfred.backoffice.modules.auth.application.dto.request.UserSignup;
import com.alfred.backoffice.modules.auth.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

}

package com.alfred.backoffice.modules.auth.application.service;

import com.alfred.backoffice.modules.auth.domain.model.User;
import com.alfred.backoffice.modules.auth.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthorizationService {

    private final UserService userService;

    @SneakyThrows
    public boolean hasAccess(Authentication authentication, int level) {
        // TODO: Add also validation for user' status
        User user = userService.getUser((String) authentication.getPrincipal());
        return user.getUserTypes().stream().anyMatch(userType -> userType.getLevel() <= level);
    }
}

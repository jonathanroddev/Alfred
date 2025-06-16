package com.alfred.backoffice.modules.auth.application.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class SignupRequest {
    @Valid
    private List<UserSignup> users;
    private String communityId;
}

package com.alfred.backoffice.modules.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class SignupRequest {
    private List<UserSignup> users;
    private String communityId;
}

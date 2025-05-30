package com.alfred.backoffice.modules.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class SignupResponse {
    private Map<String, SignupStatus> result;
    private String communityId;
}

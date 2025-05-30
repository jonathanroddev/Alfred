package com.alfred.backoffice.modules.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SignupStatus {
    String code;
    String message;
}

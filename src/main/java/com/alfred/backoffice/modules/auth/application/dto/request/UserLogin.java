package com.alfred.backoffice.modules.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserLogin {
    @NotBlank(message = "amg-400_7")
    @Email(message = "amg-400_9")
    private String email;

    @NotBlank(message = "amg-400_8")
    private String password;
}

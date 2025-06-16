package com.alfred.backoffice.modules.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Registry {
    @NotBlank(message = "amg-400_7")
    @Email(message = "amg-400_9")
    private String mail;

    private String comment;
}

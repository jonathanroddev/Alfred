package com.alfred.backoffice.modules.auth.application.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class PlanDTO {
    @NotBlank(message = "amg-400_2")
    private String name;
    @NotEmpty(message = "amg-400_3")
    private Set<ResourceDTO> resources;
}

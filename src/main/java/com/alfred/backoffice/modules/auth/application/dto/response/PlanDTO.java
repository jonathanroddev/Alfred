package com.alfred.backoffice.modules.auth.application.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Set;

@RequiredArgsConstructor
@Data
public class PlanDTO {
    private String name;
    private Set<ResourceDTO> resources;
}

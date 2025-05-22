package com.alfred.backoffice.modules.auth.application.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
@Data
public class CommunityDTO {
    @Nullable
    private String uuid;
    @NotBlank(message = "amg-400_4")
    private String name;
    @Valid
    private PlanDTO plan;
}
